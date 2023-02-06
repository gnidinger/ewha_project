package com.ewha.back.domain.feed.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ewha.back.domain.feed.dto.FeedDto;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.feed.mapper.FeedMapper;
import com.ewha.back.domain.feed.repository.FeedQueryRepository;
import com.ewha.back.domain.feed.service.FeedService;
import com.ewha.back.domain.image.service.AwsS3Service;
import com.ewha.back.global.config.CustomPage;
import com.ewha.back.global.dto.SingleResponseDto;
import com.ewha.back.global.security.jwtTokenizer.JwtTokenizer;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
public class FeedController {
	private final FeedMapper feedMapper;
	private final FeedService feedService;
	private final AwsS3Service awsS3Service;
	private final JwtTokenizer jwtTokenizer;
	private final FeedQueryRepository feedQueryRepository;

	@PostMapping("/add")
	public ResponseEntity postFeed(@Nullable @RequestParam(value = "image") MultipartFile multipartFile,
		@Valid @RequestPart(value = "post") FeedDto.Post postFeed) throws Exception {

		List<String> imagePath = null;

		Feed feed = feedMapper.feedPostToFeed(postFeed);
		Feed createdFeed = feedService.createFeed(feed);
		createdFeed.addFeedCategories(feed.getFeedCategories());

		if (multipartFile != null) {
			imagePath = awsS3Service.uploadImageToS3(multipartFile, createdFeed.getId());
			createdFeed.addImagePaths(imagePath.get(0), imagePath.get(1));
		}


		FeedDto.Response response = feedMapper.feedToFeedResponse(createdFeed);

		return new ResponseEntity<>(
			new SingleResponseDto<>(response), HttpStatus.CREATED
		);
	}

	@PatchMapping("/{feed_id}/edit")
	public ResponseEntity patchFeed(@PathVariable("feed_id") @Positive Long feedId,
		@Nullable @RequestParam(value = "image") MultipartFile multipartFile,
		@Valid @RequestPart(value = "patch") FeedDto.Patch patchFeed) throws Exception {

		List<String> imagePath = null;

		if (multipartFile != null) {
			imagePath = awsS3Service.updateORDeleteFeedImageFromS3(feedId, multipartFile);
		}

		patchFeed.setImagePath(imagePath.get(0));
		patchFeed.setThumbnailPath(imagePath.get(1));
		Feed feed = feedMapper.feedPatchToFeed(patchFeed);
		Feed updatedFeed = feedService.updateFeed(feed, feedId);
		updatedFeed.addFeedCategories(feed.getFeedCategories());
		FeedDto.Response response = feedMapper.feedToFeedResponse(updatedFeed);

		return new ResponseEntity<>(
			new SingleResponseDto<>(response), HttpStatus.OK
		);
	}

	@GetMapping("/{feed_id}")
	public ResponseEntity getFeed(
		@PathVariable("feed_id") @Positive Long feedId,
		HttpServletRequest request,
		@RequestHeader(value = "Authorization", required = false) @Valid @Nullable String token) {

		FeedDto.Response response;

		if (jwtTokenizer.checkUserWithToken(request, token)) { // 로그인 사용자
			// 로그인 사용자이면서 Auth가 있는 경우
			Feed feed = feedService.updateView(feedId);
			Feed isLikedComments = feedService.isLikedComments(feedId);

			response = feedMapper.feedToFeedResponse(feed);

			return new ResponseEntity<>(
				new SingleResponseDto<>(response), HttpStatus.OK);
		}

		// 비로그인 사용자
		Feed feed = feedService.updateView(feedId);
		feed.setIsLiked(null);

		response = feedMapper.feedToFeedResponse(feed);

		return new ResponseEntity<>(
			new SingleResponseDto<>(response), HttpStatus.OK);

	}

	@GetMapping("/newest")
	public ResponseEntity getFeeds(@RequestParam(name = "page", defaultValue = "1") int page) {

		List<Feed> feedList = feedService.findNewestFeeds();
		PageRequest pageRequest = PageRequest.of(page - 1, 10);
		List<FeedDto.ListResponse> responses = feedMapper.TESTnewFeedsToPageResponse(feedList);
		// PageImpl<FeedDto.ListResponse> responsePage = new PageImpl<>(responses, pageRequest, (long)responses.size());

		return ResponseEntity.ok().body(responses);
		// return ResponseEntity.ok().body(PagedModel.of(EntityModel.of(responsePage)));
	}

	// @GetMapping("/newest")
	// public ResponseEntity getFeeds(@RequestParam(name = "page", defaultValue = "1") int page) {
	//
	// 	CustomPage<Feed> feedList = feedService.findNewestFeeds(page);
	// 	CustomPage<FeedDto.ListResponse> responses = feedMapper.TESTnewFeedsToPageResponse(feedList);
	//
	// 	return new ResponseEntity<>(
	// 		new SingleResponseDto<>(responses), HttpStatus.OK);
	// }

	@GetMapping("/categories")
	public ResponseEntity getCategoryFeeds(@RequestParam("category") String categoryName,
		@RequestParam(name = "page", defaultValue = "1") int page) {

		Page<Feed> feedList = feedService.findCategoryFeeds(categoryName, page);
		PageImpl<FeedDto.ListResponse> responses = feedMapper.newFeedsToPageResponse(feedList);

		return new ResponseEntity<>(
			new SingleResponseDto<>(responses), HttpStatus.OK);
	}

	@DeleteMapping("/{feed_id}/delete")
	public ResponseEntity deleteFeed(
		@PathVariable("feed_id") @Positive Long feedId) {

		feedService.deleteFeed(feedId);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
