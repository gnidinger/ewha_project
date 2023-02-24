// package com.ewha.back.global.aop;
//
// import org.aspectj.lang.JoinPoint;
// import org.aspectj.lang.annotation.AfterReturning;
// import org.aspectj.lang.annotation.Aspect;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Component;
//
// import com.ewha.back.domain.comment.dto.CommentDto;
// import com.ewha.back.domain.comment.entity.Comment;
// import com.ewha.back.domain.comment.mapper.CommentMapper;
// import com.ewha.back.domain.comment.service.CommentService;
// import com.ewha.back.domain.feed.entity.Feed;
// import com.ewha.back.domain.feed.service.FeedService;
// import com.ewha.back.domain.notification.service.NotificationService;
// import com.ewha.back.domain.user.service.UserService;
//
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// @Aspect
// @Component
// @RequiredArgsConstructor
// public class SpringAop {
// 	private final NotificationService notificationService;
// 	private final UserService userService;
// 	private final FeedService feedService;
// 	private final CommentService commentService;
// 	private final CommentMapper commentMapper;
//
// 	@AfterReturning(value = "Pointcuts.createLike()", returning = "response")
// 	public void createLikeNotification(JoinPoint joinPoint, ResponseEntity response) {
//
// 		String method = joinPoint.getSignature().getName(); // 좋아요 달린 대상
// 		Long idx = Long.parseLong(joinPoint.getArgs()[0].toString()); // 대상 ID
//
// 		if (method.equals("postFeedLike")) {
//
// 			Feed feed = feedService.findVerifiedFeed(idx);
// 			notificationService.notifyUpdateLikeFeedEvent(feed);
//
// 			log.info("Feed Like Notification Sent");
//
// 		} else if (method.equals("postCommentLike")) {
//
// 			Comment comment = commentService.findVerifiedComment(idx);
// 			notificationService.notifyUpdateLikeCommentEvent(comment);
//
// 			log.info("Comment Like Notification Sent");
// 		}
// 	}
//
// 	@AfterReturning(value = "Pointcuts.createFeedComment() && args(feedId, postComment)", returning = "response")
// 	public void createCommentNotification(JoinPoint joinPoint, Long feedId,
// 		CommentDto.Post postComment, ResponseEntity<CommentDto.Response> response) { // 코멘트 알림 보내기
//
// 		String method = joinPoint.getSignature().getName(); // 좋아요 달린 대상
// 		long idx = Long.parseLong(joinPoint.getArgs()[0].toString()); // 대상 ID
// 		CommentDto.Post commentDto = (CommentDto.Post)joinPoint.getArgs()[1]; // 달린 댓글 DTO
//
// 		Comment createdComment = commentService.findVerifiedComment(response.getBody().getCommentId());
// 		notificationService.notifyPostPairingCommentEvent(createdComment);
//
// 		log.info("Pairing Comment Notification Sent");
// 	}
// }
