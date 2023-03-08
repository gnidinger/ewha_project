package com.ewha.back.domain.image.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import com.ewha.back.domain.image.entity.Image;
import com.ewha.back.domain.image.service.ImageService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

	private final ImageService imageService;

	@PostMapping("/add")
	public String postImage(@RequestParam("image") MultipartFile multipartFile) throws IOException {

		Long savedImageId = imageService.saveImage(multipartFile);

		Image savedImage = imageService.findVerifiedImage(savedImageId);

		System.out.println(savedImage.getStoredPath());

		return "redirect:/";
	}

	@GetMapping("/{image_id}")
	public Resource getImage(@PathVariable("image_id") Long imageId) throws IOException {

		Image image = imageService.findVerifiedImage(imageId);

		return new UrlResource("image: " + image.getStoredPath());
	}

	@GetMapping("/{image_id}/download")
	public ResponseEntity downloadImage(@PathVariable("image_id") Long imageId) throws MalformedURLException {

		Image image = imageService.findVerifiedImage(imageId);

		UrlResource urlResource = new UrlResource("image: " + image.getStoredPath());

		String encodedImageName = UriUtils.encode(image.getOriginalImageName(), StandardCharsets.UTF_8);

		String imageDisposition = "attachment; filename=\"" + encodedImageName + "\"";

		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, imageDisposition)
			.body(urlResource);
	}
}
