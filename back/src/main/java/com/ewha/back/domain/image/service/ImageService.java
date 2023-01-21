package com.ewha.back.domain.image.service;

import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.image.entity.Image;
import com.ewha.back.domain.image.repository.ImageQueryRepository;
import com.ewha.back.domain.image.repository.ImageRepository;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.service.UserService;
import com.ewha.back.global.exception.BusinessLogicException;
import com.ewha.back.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import marvin.image.MarvinImage;
import org.marvinproject.image.transform.scale.Scale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {
    private final UserService userService;
    private final ImageRepository imageRepository;
    private final ImageQueryRepository imageQueryRepository;

    @Value("")
    private String imageDir;

    public Long saveImage(MultipartFile multipartFile) throws IOException {

        User findUser = userService.getLoginUser();

        if (multipartFile.isEmpty()) return null;

        String originalImageName = multipartFile.getOriginalFilename(); // 업로드 파일 이름

        String uuid = UUID.randomUUID().toString(); // 파일 저장 이름으로 사용할 UUID 생성

        String extension = originalImageName.substring(originalImageName.lastIndexOf(".")); // 확장자 추출

        String storedImageName = uuid + extension; // 저장 파일 이름 + 확장자

        String storedPath = imageDir + storedImageName; // 파일 저장 경로

        Image image = Image.builder() // 이미지 객체 생성
                .originalImageName(originalImageName)
                .storedImageName(storedImageName)
                .storedPath(storedPath)
                .user(findUser)
                .build();

        multipartFile.transferTo(new File(storedPath)); // 로컬에 저장

        Image storedImage = imageRepository.save(image);

        return storedImage.getId();
    }

    public Long saveFeedImage(MultipartFile multipartFile, Feed feed) throws IOException {

        User findUser = userService.getLoginUser();

        if (multipartFile.isEmpty()) return null;

        String originalImageName = multipartFile.getOriginalFilename(); // 업로드 파일 이름

        String uuid = UUID.randomUUID().toString(); // 파일 저장 이름으로 사용할 UUID 생성

        String extension = originalImageName.substring(originalImageName.lastIndexOf(".")); // 확장자 추출

        String storedImageName = uuid + extension; // 저장 파일 이름 + 확장자

        String storedPath = imageDir + storedImageName; // 파일 저장 경로

        Image image = Image.builder() // 이미지 객체 생성
                .originalImageName(originalImageName)
                .storedImageName(storedImageName)
                .storedPath(storedPath)
                .user(findUser)
                .feed(feed)
                .build();

        multipartFile.transferTo(new File(storedPath)); // 로컬에 저장

        Image storedImage = imageRepository.save(image);

        return storedImage.getId();
    }

    public void deleteImage(Long imageId) {
        imageRepository.deleteById(imageId);
    }

    public void deleteFeedImage(Long feedId) {
        imageQueryRepository.deleteByFeedId(feedId);
    }

    public void deleteProfileImage() {

        User findUser = userService.getLoginUser();
        String profileImage = findUser.getProfileImage();

        imageQueryRepository.deleteByImagePath(profileImage);
    }

    public void validateFileExists(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.IMAGE_NOT_FOUND);
        }
    }

    public Image findVerifiedImage(Long imageId) {

        return imageRepository.findById(imageId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.IMAGE_NOT_FOUND));
    }

    public MultipartFile resizeImage(MultipartFile multipartFile, String extension,
                                     String storedImageName, String requestURI) throws Exception {

        BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int limit = 0;

        if (requestURI.equals("/feeds/add")) limit = 768;
        else if (requestURI.equals("/mypage/userinfo")) limit = 320;

        MarvinImage marvinImage = new MarvinImage(bufferedImage);

        Scale scale = new Scale();

        if (width <= limit && height <= limit) {
            return multipartFile;
        } else if (width <= limit && height > limit) {
            scale.load();
            scale.setAttribute("newHeight", limit);
            scale.setAttribute("newWidth", limit * width / height);
        } else if (width > limit && height <= limit) {
            scale.load();
            scale.setAttribute("newHeight", limit * height / width);
            scale.setAttribute("newWidth", limit);
        } else if (width > limit && height > limit) {
            if (width <= height) {
                scale.load();
                scale.setAttribute("newHeight", limit);
                scale.setAttribute("newWidth", limit * width / height);
            } else if (width >= height) {
                scale.load();
                scale.setAttribute("newHeight", limit * height / width);
                scale.setAttribute("newWidth", limit);
            }
        }

        scale.process(marvinImage.clone(), marvinImage, null, null, false);

        BufferedImage imageNolpha = marvinImage.getBufferedImageNoAlpha();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(imageNolpha, extension, byteArrayOutputStream);

        byteArrayOutputStream.flush();

        return new MockMultipartFile(storedImageName, byteArrayOutputStream.toByteArray());
    }
}
