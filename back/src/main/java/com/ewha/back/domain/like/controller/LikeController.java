package com.ewha.back.domain.like.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ewha.back.domain.comment.entity.Comment;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.like.service.LikeService;
import com.ewha.back.domain.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping
@RequiredArgsConstructor
public class LikeController {
	private final LikeService likeService;
	private final NotificationService notificationService;

	@PatchMapping("/feeds/{feed_id}/like")
	public ResponseEntity<HttpStatus> postFeedLike(@PathVariable("feed_id") Long feedId) {

		Feed likedFeed = likeService.createFeedLike(feedId);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PatchMapping("/feeds/{feed_id}/dislike")
	public ResponseEntity<HttpStatus> deleteFeedLike(@PathVariable("feed_id") Long feedId) {

		Feed dislikedFeed = likeService.deleteFeedLike(feedId);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PatchMapping("/comments/{comment_id}/like")
	public ResponseEntity<HttpStatus> postCommentLike(@PathVariable("comment_id") Long commentId) {

		Comment likedComment = likeService.createCommentLike(commentId);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PatchMapping("/comments/{comment_id}/dislike")
	public ResponseEntity<HttpStatus> deleteCommentLike(@PathVariable("comment_id") Long commentId) {

		Comment dislikedComment = likeService.deleteCommentLike(commentId);

		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
