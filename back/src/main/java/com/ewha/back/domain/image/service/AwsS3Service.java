package com.ewha.back.domain.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.feed.repository.FeedRepository;
import com.ewha.back.domain.feed.service.FeedService;
import com.ewha.back.domain.image.entity.Image;
import com.ewha.back.domain.image.entity.ImageType;
import com.ewha.back.domain.image.repository.ImageRepository;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.repository.UserRepository;
import com.ewha.back.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.name.Rename;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AwsS3Service {
    private final AmazonS3Client amazonS3Client;
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final FeedService feedService;
    private final FeedRepository feedRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private ImageType imageType;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadImageToS3(MultipartFile multipartFile, Long id) throws Exception {

        HttpServletRequest httpServletRequest =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String requestURI = httpServletRequest.getRequestURI(); // 요청 URI. 피드 사진인지 프로필 사진인지 분기용

        String storedPath = null;

        Feed feed;
        User user;

        if (requestURI.equals("/feeds/add")) {
            storedPath = "feedImages/";
            imageType = ImageType.FEED;
            feed = feedService.findVerifiedFeed(id);
        } else if (requestURI.equals("/mypage/userinfo")) {
            storedPath = "profileImages/";
            imageType = ImageType.PROFILE_PICTURE;
            user = userService.findVerifiedUser(id);
        }

        imageService.validateFileExists(multipartFile);

        if (multipartFile.isEmpty()) return null;

        String originalImageName = multipartFile.getOriginalFilename(); // 원래 파일 이름

        String uuid = UUID.randomUUID().toString(); // 파일 이름으로 사용할 UUID 생성

        String extension = multipartFile.getContentType()
                .substring(multipartFile.getContentType().lastIndexOf("/") + 1); // 확장자 추출

        String[] extensions = {"png", "jpg", "jpeg"}; // 지원할 포맷

        if (!Arrays.asList(extensions).contains(extension)) {
            throw new IllegalArgumentException("지원하지 않는 포맷입니다.");
        }

        String storedImageName = uuid + '.' + extension; // 파일 이름 + 확장자

        MultipartFile resizedFile = imageService.resizeImage(multipartFile, extension, storedImageName, requestURI);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(resizedFile.getSize());
        objectMetadata.setContentType(resizedFile.getContentType());

        InputStream inputStream = resizedFile.getInputStream();

        amazonS3Client.putObject(new PutObjectRequest(bucketName, storedPath + storedImageName, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        String fullPath = amazonS3Client.getUrl(bucketName, storedPath + storedImageName).toString();
        String thumbnailPath = uploadThumbnailToAwsS3(resizedFile, storedImageName);

        Image.ImageBuilder image = Image.builder();

        image.originalImageName(originalImageName)
                .storedImageName(storedImageName)
                .storedPath(fullPath);

        if (requestURI.equals("/feeds/add")) {

            Feed findFeed = feedService.findVerifiedFeed(id);
            image.imageType(ImageType.FEED)
                    .thumbnailPath(thumbnailPath)
                    .feed(findFeed)
                    .user(findFeed.getUser());

            findFeed.addImagePaths(fullPath, thumbnailPath);

            feedRepository.save(findFeed);

        } else if (requestURI.equals("/mypage/userinfo")) {

            User findUser = userService.findVerifiedUser(id);

            image.imageType(ImageType.PROFILE_PICTURE)
                    .thumbnailPath(thumbnailPath)
                    .user(findUser)
                    .feed(null);

            findUser.setProfileImage(fullPath);
            findUser.setThumbnailPath(thumbnailPath);

            userRepository.save(findUser);
        }

        Image storedImage = imageRepository.save(image.build());

        return amazonS3Client.getUrl(bucketName, storedPath + storedImageName).toString();
    }

    public String updateORDeleteFeedImageFromS3(Long feedId, MultipartFile multipartFile) throws Exception {

        Feed findFeed = feedService.findVerifiedFeed(feedId);
        String feedImagePath = findFeed.getImagePath();
        String imageName = feedImagePath.substring(feedImagePath.lastIndexOf("/"));
        String newImagePath = null;

        if (feedImagePath == null && multipartFile != null) {
            newImagePath = uploadImageToS3(multipartFile, feedId);
        } else if (feedImagePath != null && multipartFile != null) {
            deleteImageFromS3(imageName);
            newImagePath = uploadImageToS3(multipartFile, feedId);
        }

        return newImagePath;
    }

    public String uploadThumbnailToAwsS3(MultipartFile multipartFile, String storedImageName) throws IOException {

        BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());
        Integer getWidth = bufferedImage.getWidth();
        Integer getHeight = bufferedImage.getHeight();
        Double ratio = (double) (getWidth / getHeight);

        BufferedImage thumbnailImage = Thumbnails.of(bufferedImage)
                .size(100, 100)
                .asBufferedImage();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(thumbnailImage, "png", os);

        byte[] bytes = os.toByteArray();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(bytes.length);
        objectMetadata.setContentType("image/png");

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, "thumbnailImages/"+ "s_" + storedImageName, byteArrayInputStream, objectMetadata);
        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3Client.putObject(putObjectRequest);

        return amazonS3Client.getUrl(bucketName, "thumbnailImages/" + "s_" + storedImageName).toString();
    }

    public void deleteImageFromS3(String imageName) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, imageName));
    }
}
