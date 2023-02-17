package com.ewha.back.domain.feed.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ewha.back.domain.feed.dto.FeedDto;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.feed.mapper.FeedMapper;
import com.ewha.back.domain.feed.service.FeedService;
import com.ewha.back.domain.image.service.AwsS3Service;
import com.ewha.back.domain.like.entity.Like;
import com.ewha.back.domain.like.service.LikeService;
import com.ewha.back.global.config.CustomPage;
import com.ewha.back.global.security.jwtTokenizer.JwtTokenizer;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
public class FeedController {
	private final FeedMapper feedMapper;
	private final FeedService feedService;
	private final LikeService likeService;
	private final AwsS3Service awsS3Service;
	private final JwtTokenizer jwtTokenizer;

	@PostMapping("/add")
	public ResponseEntity<HttpStatus> postFeed(
		@Nullable @RequestParam(value = "image") MultipartFile multipartFile,
		@Valid @RequestPart(value = "post") FeedDto.Post postFeed) throws Exception {

		List<String> imagePath = null;

		Feed feed = feedMapper.feedPostToFeed(postFeed);
		Feed createdFeed = feedService.createFeed(feed);

		if (multipartFile != null) {
			imagePath = awsS3Service.uploadImageToS3(multipartFile, createdFeed.getId());
			createdFeed.addImagePaths(imagePath.get(0), imagePath.get(1));
		}

		// FeedDto.Response response = feedMapper.feedToFeedResponse(createdFeed);
		createdFeed.addFeedCategories(feed.getFeedCategories());
		// return ResponseEntity.status(HttpStatus.CREATED).body(response);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PatchMapping("/{feed_id}/edit")
	public ResponseEntity<HttpStatus> patchFeed(@PathVariable("feed_id") @Positive Long feedId,
		@Nullable @RequestParam(value = "image") MultipartFile multipartFile,
		@Valid @RequestPart(value = "patch") FeedDto.Patch patchFeed) throws Exception {

		List<String> imagePath = null;

		Feed feed = feedMapper.feedPatchToFeed(patchFeed);
		Feed updatedFeed = feedService.updateFeed(feed, feedId);

		if (updatedFeed.getImagePath() != null && patchFeed.getImagePath() != null
			&& multipartFile == null && patchFeed.getImagePath().equals(updatedFeed.getImagePath())) {
			updatedFeed.addImagePaths(updatedFeed.getImagePath(), updatedFeed.getThumbnailPath());
		} else if (patchFeed.getImagePath() == null && multipartFile != null) {
			imagePath = awsS3Service.uploadImageToS3(multipartFile, updatedFeed.getId());
			updatedFeed.addImagePaths(imagePath.get(0), imagePath.get(1));
		} else if (updatedFeed.getImagePath() != null && multipartFile == null
			&& patchFeed.getImagePath() == null) {
			awsS3Service.updateORDeleteFeedImageFromS3(updatedFeed.getId(), multipartFile);
			updatedFeed.addImagePaths(null, null);
		}

		feedService.saveFeed(updatedFeed);

		updatedFeed.addFeedCategories(feed.getFeedCategories());
		// FeedDto.Response response = feedMapper.feedToFeedResponse(updatedFeed);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping("/{feed_id}")
	public ResponseEntity<FeedDto.Response> getFeed(
		@PathVariable("feed_id") @Positive Long feedId,
		HttpServletRequest request,
		@RequestHeader(value = "Authorization", required = false) @Valid @Nullable String token) {

		FeedDto.Response response;

		if (jwtTokenizer.checkUserWithToken(request, token)) { // 로그인 사용자
			// 로그인 사용자이면서 Auth가 있는 경우
			Feed feed = feedService.updateView(feedId);
			Boolean isLikedFeed = likeService.isLikedFeed(feed);
			List<Like> isLikedComments = feedService.isLikedComments(feedId);

			response = feedMapper.feedGetToFeedResponse(feed, isLikedFeed, isLikedComments);

			return ResponseEntity.status(HttpStatus.OK).body(response);
		}

		// 비로그인 사용자
		Feed feed = feedService.updateView(feedId);

		response = feedMapper.feedGetWithoutLoginToFeedResponse(feed);

		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

	@GetMapping("/newest")
	public ResponseEntity<CustomPage<FeedDto.ListResponse>> getFeeds(
		@RequestParam(name = "page", defaultValue = "1") int page) {

		CustomPage<Feed> feedList = feedService.findNewestFeeds(page);
		CustomPage<FeedDto.ListResponse> responses = feedMapper.TESTnewFeedsToPageResponse(feedList);

		return ResponseEntity.status(HttpStatus.OK).body(responses);
	}

	@GetMapping("/categories")
	public ResponseEntity<PageImpl<FeedDto.ListResponse>> getCategoryFeeds(
		@RequestParam("category") String categoryName,
		@RequestParam(name = "page", defaultValue = "1") int page) {

		Page<Feed> feedList = feedService.findCategoryFeeds(categoryName, page);
		PageImpl<FeedDto.ListResponse> responses = feedMapper.newFeedsToPageResponse(feedList);

		return ResponseEntity.status(HttpStatus.OK).body(responses);
	}

	@DeleteMapping("/{feed_id}/delete")
	public ResponseEntity<String> deleteFeed(
		@PathVariable("feed_id") @Positive Long feedId) {

		feedService.deleteFeed(feedId);

		return ResponseEntity.noContent().build();
	}
}
