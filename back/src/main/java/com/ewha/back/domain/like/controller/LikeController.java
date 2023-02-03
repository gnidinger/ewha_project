package com.ewha.back.domain.like.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ewha.back.domain.comment.dto.CommentDto;
import com.ewha.back.domain.comment.entity.Comment;
import com.ewha.back.domain.comment.mapper.CommentMapper;
import com.ewha.back.domain.feed.dto.FeedDto;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.feed.mapper.FeedMapper;
import com.ewha.back.domain.like.service.LikeService;
import com.ewha.back.global.dto.SingleResponseDto;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping
@RequiredArgsConstructor
public class LikeController {
	private final LikeService likeService;
	private final FeedMapper feedMapper;
	private final CommentMapper commentMapper;

	@PatchMapping("/feeds/{feed_id}/like")
	public ResponseEntity postFeedLike(@PathVariable("feed_id") Long feedId) {

		Feed likedFeed = likeService.createFeedLike(feedId);
		FeedDto.Response response = feedMapper.feedToFeedResponse(likedFeed);

		return new ResponseEntity<>(
			new SingleResponseDto<>(response), HttpStatus.OK
		);
	}

	@PatchMapping("/feeds/{feed_id}/dislike")
	public ResponseEntity deleteFeedLike(@PathVariable("feed_id") Long feedId) {

		Feed dislikedFeed = likeService.deleteFeedLike(feedId);
		FeedDto.Response response = feedMapper.feedToFeedResponse(dislikedFeed);

		return new ResponseEntity<>(
			new SingleResponseDto<>(response), HttpStatus.OK
		);
	}

	@PatchMapping("/comments/{comment_id}/like")
	public ResponseEntity postCommentLike(@PathVariable("comment_id") Long commentId) {

		Comment likedComment = likeService.createCommentLike(commentId);
		CommentDto.Response response = commentMapper.commentToCommentResponse(likedComment);

		return new ResponseEntity<>(
			new SingleResponseDto<>(response), HttpStatus.OK
		);
	}

	@PatchMapping("/comments/{comment_id}/dislike")
	public ResponseEntity deleteCommentLike(@PathVariable("comment_id") Long commentId) {

		Comment dislikedComment = likeService.deleteCommentLike(commentId);
		CommentDto.Response response = commentMapper.commentToCommentResponse(dislikedComment);

		return new ResponseEntity<>(
			new SingleResponseDto<>(response), HttpStatus.OK
		);
	}
}
