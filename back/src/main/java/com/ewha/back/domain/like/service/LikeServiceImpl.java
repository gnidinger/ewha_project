package com.ewha.back.domain.like.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ewha.back.domain.comment.entity.Comment;
import com.ewha.back.domain.comment.service.CommentService;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.feed.service.FeedService;
import com.ewha.back.domain.like.entity.Like;
import com.ewha.back.domain.like.entity.LikeType;
import com.ewha.back.domain.like.repository.LikeQueryRepository;
import com.ewha.back.domain.like.repository.LikeRepository;
import com.ewha.back.domain.notification.service.NotificationService;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.service.UserService;
import com.ewha.back.global.exception.BusinessLogicException;
import com.ewha.back.global.exception.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
	private final UserService userService;
	private final FeedService feedService;
	private final CommentService commentService;
	private final LikeRepository likeRepository;
	private final LikeQueryRepository likeQueryRepository;
	private final NotificationService notificationService;

	@Override
	public Feed createFeedLike(Long feedId) {

		User findUser = userService.getLoginUser();

		Feed findFeed = feedService.findVerifiedFeed(feedId);

		Like findFeedLike = likeQueryRepository.findFeedLikeByFeedAndUser(findFeed, findUser);

		if (findFeedLike == null) {
			findFeedLike = Like.builder()
				.likeType(LikeType.FEED)
				.user(findUser)
				.feed(findFeed)
				.build();

			likeRepository.save(findFeedLike);

			// notificationService.notifyUpdateLikeFeedEvent(findFeed);

			findFeed.addLike();

			return findFeed;
		} else {
			throw new BusinessLogicException(ExceptionCode.LIKED);
		}
	}

	@Override
	public Feed deleteFeedLike(Long feedId) {

		User findUser = userService.getLoginUser();

		Feed findFeed = feedService.findVerifiedFeed(feedId);

		Like findFeedLike = likeQueryRepository.findFeedLikeByFeedAndUser(findFeed, findUser);

		if (findFeedLike == null) {
			throw new BusinessLogicException(ExceptionCode.UNLIKED);
		} else {
			likeRepository.delete(findFeedLike);
		}

		findFeed.removeLike();

		return findFeed;
	}

	@Override
	public Comment createCommentLike(Long commentId) {

		User findUser = userService.getLoginUser();

		Comment findComment = commentService.findVerifiedComment(commentId);

		Like findCommentLike = likeQueryRepository.findCommentLikeByFeedAndUser(findComment, findUser);

		if (findCommentLike == null) {
			findCommentLike = Like.builder()
				.likeType(LikeType.COMMENT)
				.user(findUser)
				.comment(findComment)
				.build();

			likeRepository.save(findCommentLike);

			findComment.addLike();

			return findComment;
		} else
			throw new BusinessLogicException(ExceptionCode.LIKED);
	}

	@Override
	public Comment deleteCommentLike(Long commentId) {

		User findUser = userService.getLoginUser();

		Comment findComment = commentService.findVerifiedComment(commentId);

		Like findCommentLike = likeQueryRepository.findCommentLikeByFeedAndUser(findComment, findUser);

		if (findCommentLike == null) {

			throw new BusinessLogicException(ExceptionCode.UNLIKED);
		} else {
			likeRepository.delete(findCommentLike);
		}

		findComment.removeLike();

		return findComment;
	}

	@Override
	public Boolean isLikedFeed(Feed feed) {
		User findUser = userService.getLoginUser();
		return likeQueryRepository.findFeedLikeByFeedAndUser(feed, findUser) != null;
	}

	@Override
	public Boolean isLikedComment(Comment comment) {
		User findUser = userService.getLoginUser();
		return likeQueryRepository.findCommentLikeByFeedAndUser(comment, findUser) != null;
	}
}
