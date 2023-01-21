package com.ewha.back.domain.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AwsS3Service {
    private final AmazonS3Client  amazonS3Client;
    private final ImageService imageService;
    private final FeedService feedService;

    @Value("{$cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadImageToS3(MultipartFile multipartFile) throws Exception {

        HttpServletRequest httpServletRequest =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String requestURI = httpServletRequest.getRequestURI(); // 요청 URI. 피드 사진인지 프로필 사진인지 분기용

        String storedPath = null;

        if (requestURI.equals("/feeds/add")) storedPath = "feedImages/";
        else if (requestURI.equals("/mypage/userinfo")) storedPath = "profileImages/";

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

        String storedImageName = uuid +'.'+ extension; // 파일 이름 + 확장자

        MultipartFile resizedFile = imageService.resizeImage(multipartFile, extension, storedImageName, requestURI);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(resizedFile.getSize());
        objectMetadata.setContentType(resizedFile.getContentType());

        InputStream inputStream = resizedFile.getInputStream();

        amazonS3Client.putObject(new PutObjectRequest(bucketName, storedPath + storedImageName, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return amazonS3Client.getUrl(bucketName, storedPath + storedImageName).toString();
    }

    public String updateORDeleteFeedImageFromS3(Long feedId, MultipartFile multipartFile) throws Exception {

        Feed findFeed = feedService.findVerifiedFeed(feedId);
        String feedImagePath = findFeed.getImagePath();
        String imageName = feedImagePath.substring(feedImagePath.lastIndexOf("/"));
        String newImagePath = null;

        if (feedImagePath == null && multipartFile != null) {
            newImagePath = uploadImageToS3(multipartFile);
        }  else if (feedImagePath != null && multipartFile != null) {
            deleteImageFromS3(imageName);
            newImagePath = uploadImageToS3(multipartFile);
        }

        return newImagePath;
    }

    public void deleteImageFromS3 (String imageName) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, imageName));
    }
}
