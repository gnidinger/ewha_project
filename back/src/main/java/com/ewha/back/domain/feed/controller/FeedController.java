package com.ewha.back.domain.feed.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
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

import com.ewha.back.domain.comment.entity.Comment;
import com.ewha.back.domain.comment.service.CommentService;
import com.ewha.back.domain.feed.dto.FeedDto;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.feed.mapper.FeedMapper;
import com.ewha.back.domain.feed.service.FeedService;
import com.ewha.back.domain.image.repository.ImageQueryRepository;
import com.ewha.back.domain.image.service.AwsS3Service;
import com.ewha.back.domain.image.service.ImageService;
import com.ewha.back.domain.like.entity.Like;
import com.ewha.back.domain.like.service.LikeService;
import com.ewha.back.global.config.CustomPage;
import com.ewha.back.global.dto.MultiResponseDto;
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
	private final CommentService commentService;
	private final AwsS3Service awsS3Service;
	private final ImageQueryRepository imageQueryRepository;

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

		Feed findFeed = feedService.findVerifiedFeed(feedId);
		Feed feed = feedMapper.feedPatchToFeed(patchFeed);
		Feed updatedFeed = feedService.updateFeed(feed, feedId);

		// MultipartFile이 없으면서, 기존 피드에 이미지가 있고, 요청 JSON에도 이미지가 있고, 두 경로가 같은 경우
		if (multipartFile == null && findFeed.getImagePath() != null && patchFeed.getImagePath() != null
			&& patchFeed.getImagePath().equals(updatedFeed.getImagePath())) {
			updatedFeed.addImagePaths(updatedFeed.getImagePath(), updatedFeed.getThumbnailPath());
			// 기존 피드에 이미지가 있고 요청 JSON에 이미지가 없고 MultipartFile이 있는 경우
		} else if (findFeed.getImagePath() != null && patchFeed.getImagePath() == null && multipartFile != null) {
			imagePath = awsS3Service.updateORDeleteFeedImageFromS3(updatedFeed.getId(), multipartFile);
			updatedFeed.addImagePaths(imagePath.get(0), imagePath.get(1));
			// 기존 피드에 이미지가 없고 요청 JSON에 이미지가 없고 MultipartFile이 있는 경우
		} else if (findFeed.getImagePath() == null && patchFeed.getImagePath() == null && multipartFile != null) {
			imagePath = awsS3Service.uploadImageToS3(multipartFile, updatedFeed.getId());
			updatedFeed.addImagePaths(imagePath.get(0), imagePath.get(1));
			// 기존 피드에 이미지가 있으면서 요청 JSON에 이미지가 없고, multipartFile도 없는 경우
		} else if (findFeed.getImagePath() != null && patchFeed.getImagePath() == null && multipartFile == null) {
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

		// if (jwtTokenizer.checkUserWithToken(request, token)) { // 로그인 사용자
		if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
			// 로그인 사용자이면서 Auth가 있는 경우
			Feed feed = feedService.updateView(feedId);
			Boolean isLikedFeed = likeService.isLikedFeed(feed);
			List<Like> isLikedComments = feedService.isLikedComments(feedId);
			Boolean isMyFeed = feedService.isMyFeed(feed);
			List<Comment> isMyComments = commentService.isMyComments(feedId);

			response = feedMapper.feedGetToFeedResponse(feed, isLikedFeed, isLikedComments, isMyFeed, isMyComments);

			return ResponseEntity.status(HttpStatus.OK).body(response);
		}

		// 비로그인 사용자
		Feed feed = feedService.updateView(feedId);

		response = feedMapper.feedGetWithoutLoginToFeedResponse(feed);

		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

	@GetMapping("/newest")
	public ResponseEntity<MultiResponseDto<FeedDto.ListResponse>> getFeeds(
		@RequestParam(name = "page", defaultValue = "1") int page) {

		CustomPage<Feed> feedList = feedService.findNewestFeeds(page);
		CustomPage<FeedDto.ListResponse> responses = feedMapper.TESTnewFeedsToPageResponse(feedList);

		return ResponseEntity.ok(new MultiResponseDto<>(responses.getContent(), feedList));
		// return ResponseEntity.status(HttpStatus.OK).body(feedList);
	}

	@GetMapping("/categories")
	public ResponseEntity<MultiResponseDto<FeedDto.ListResponse>> getCategoryFeeds(
		@RequestParam("category") String categoryName,
		@RequestParam(name = "page", defaultValue = "1") int page) {

		Page<Feed> feedList = feedService.findCategoryFeeds(categoryName, page);
		PageImpl<FeedDto.ListResponse> responses = feedMapper.newFeedsToPageResponse(feedList);

		return ResponseEntity.ok(new MultiResponseDto<>(responses.getContent(), feedList));
	}

	@DeleteMapping("/{feed_id}/delete")
	public ResponseEntity<String> deleteFeed(
		@PathVariable("feed_id") @Positive Long feedId) {

		feedService.deleteFeed(feedId);

		return ResponseEntity.noContent().build();
	}
}
