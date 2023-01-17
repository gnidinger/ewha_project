package com.ewha.back.domain.feed.controller;

import com.ewha.back.domain.feed.dto.FeedDto;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.feed.mapper.FeedMapper;
import com.ewha.back.domain.feed.service.FeedService;
import com.ewha.back.global.dto.SingleResponseDto;
import com.ewha.back.global.security.jwtTokenizer.JwtTokenizer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
public class FeedController {
    private final FeedMapper feedMapper;
    private final FeedService feedService;
    private final JwtTokenizer jwtTokenizer;

    @PostMapping("/add")
    public ResponseEntity postFeed(@Valid @RequestBody FeedDto.Post postFeed) {

        Feed feed = feedMapper.feedPostToFeed(postFeed);
        Feed createdFeed = feedService.createFeed(feed);
        createdFeed.addFeedCategories(feed.getFeedCategories());
        FeedDto.Response response = feedMapper.feedToFeedResponse(createdFeed);

        return new ResponseEntity<>(
                new SingleResponseDto<>(response), HttpStatus.CREATED
        );
    }

    @PatchMapping("/{feed_id}/edit")
    public ResponseEntity patchFeed(
            @PathVariable("feed_id") @Positive Long feedId,
            @Valid @RequestBody FeedDto.Patch patchFeed) {

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

        if(jwtTokenizer.checkUserWithToken(request, token)) { // 로그인 사용자
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

        Page<Feed> feedList = feedService.findNewestFeeds(page);
        PageImpl<FeedDto.ListResponse> responses = feedMapper.newFeedsToPageResponse(feedList);

        return new ResponseEntity<>(
                new SingleResponseDto<>(responses), HttpStatus.OK);
    }

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
