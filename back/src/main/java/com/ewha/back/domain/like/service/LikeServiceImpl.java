package com.ewha.back.domain.like.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ewha.back.domain.comment.entity.Comment;
import com.ewha.back.domain.comment.repository.CommentRepository;
import com.ewha.back.domain.comment.service.CommentService;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.feed.repository.FeedRepository;
import com.ewha.back.domain.feed.service.FeedService;
import com.ewha.back.domain.like.entity.CommentLike;
import com.ewha.back.domain.like.entity.FeedLike;
import com.ewha.back.domain.like.entity.LikeType;
import com.ewha.back.domain.like.repository.CommentLikeQueryRepository;
import com.ewha.back.domain.like.repository.CommentLikeRepository;
import com.ewha.back.domain.like.repository.FeedLikeQueryRepository;
import com.ewha.back.domain.like.repository.FeedLikeRepository;
import com.ewha.back.domain.notification.entity.NotificationType;
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
	private final FeedRepository feedRepository;
	private final CommentService commentService;
	private final CommentRepository commentRepository;
	private final FeedLikeRepository feedLikeRepository;
	private final FeedLikeQueryRepository feedLikeQueryRepository;
	private final CommentLikeRepository commentLikeRepository;
	private final CommentLikeQueryRepository commentLikeQueryRepository;
	// private final LikeRepository likeRepository;
	// private final LikeQueryRepository likeQueryRepository;
	private final NotificationService notificationService;

	@Override
	@Transactional
	public String feedLike(Long feedId) {

		User findUser = userService.getLoginUser();

		Feed findFeed = feedService.findVerifiedFeed(feedId);

		FeedLike findFeedLike = feedLikeQueryRepository.findFeedLikeByFeedAndUser(findFeed, findUser);

		if (findFeedLike == null) {
			findFeedLike = FeedLike.builder()
				.likeType(LikeType.FEED)
				.user(findUser)
				.feed(findFeed)
				.build();

			feedLikeRepository.save(findFeedLike);

			findFeed.addLike();

			feedRepository.save(findFeed);

			if (!findUser.getId().equals(findFeed.getUser().getId())) {
				String body = "작성하신 피드 <" + findFeed.getTitle() + ">에 "
					+ findUser.getNickname() + "님이 좋아요를 눌렀습니다.";
				String content = findFeed.getTitle();
				String url = "http://localhost:8080/feeds/" + findFeed.getId();
				notificationService.send(findFeed.getUser(), url, body, content, NotificationType.LIKE);
			}

			return "Feed Like Added";
			// return findFeed;
		} else {

			feedLikeRepository.delete(findFeedLike);

			findFeed.removeLike();

			feedRepository.save(findFeed);

			return "Feed Like Removed";
			// return findFeed;
		}
	}

	@Override
	@Transactional
	public String commentLike(Long commentId) {

		User findUser = userService.getLoginUser();

		Comment findComment = commentService.findVerifiedComment(commentId);

		CommentLike findCommentLike = commentLikeQueryRepository.findCommentLikeByFeedAndUser(findComment, findUser);

		if (findCommentLike == null) {
			findCommentLike = CommentLike.builder()
				.likeType(LikeType.COMMENT)
				.user(findUser)
				.comment(findComment)
				.build();

			commentLikeRepository.save(findCommentLike);

			findComment.addLike();

			commentRepository.save(findComment);

			if (!findUser.getId().equals(findComment.getUser().getId())) {
				String body = "작성하신 댓글 <" + findComment.getBody() + ">에 "
					+ findUser.getNickname() + "님이 좋아요를 눌렀습니다.";
				String content = findComment.getBody();
				String url = "http://localhost:8080/comments/" + findComment.getId();
				notificationService.send(findComment.getUser(), url, body, content, NotificationType.LIKE);
			}

			return "Comment Like Added";
			// return findComment;
		} else {

			commentLikeRepository.delete(findCommentLike);

			findComment.removeLike();

			commentRepository.save(findComment);

			return "Comment Like Removed";
			// return findComment;
		}
	}

	@Override
	@Transactional
	public Feed createFeedLike(Long feedId) {

		User findUser = userService.getLoginUser();

		Feed findFeed = feedService.findVerifiedFeed(feedId);

		FeedLike findFeedLike = feedLikeQueryRepository.findFeedLikeByFeedAndUser(findFeed, findUser);

		if (findFeedLike == null) {
			findFeedLike = FeedLike.builder()
				.likeType(LikeType.FEED)
				.user(findUser)
				.feed(findFeed)
				.build();

			feedLikeRepository.save(findFeedLike);

			findFeed.addLike();

			if (!findUser.getId().equals(findFeed.getUser().getId())) {
				String body = "작성하신 피드 <" + findFeed.getTitle() + ">에 "
					+ findUser.getNickname() + "님이 좋아요를 눌렀습니다.";
				String content = findFeed.getTitle();
				String url = "http://localhost:8080/feeds/" + findFeed.getId();
				notificationService.send(findFeed.getUser(), url, body, content, NotificationType.LIKE);
			}

			return findFeed;
		} else {
			throw new BusinessLogicException(ExceptionCode.LIKED);
		}
	}

	@Override
	@Transactional
	public Feed deleteFeedLike(Long feedId) {

		User findUser = userService.getLoginUser();

		Feed findFeed = feedService.findVerifiedFeed(feedId);

		FeedLike findFeedLike = feedLikeQueryRepository.findFeedLikeByFeedAndUser(findFeed, findUser);

		if (findFeedLike == null) {
			throw new BusinessLogicException(ExceptionCode.UNLIKED);
		} else {
			feedLikeRepository.delete(findFeedLike);
		}

		findFeed.removeLike();

		return findFeed;
	}

	@Override
	@Transactional
	public Comment createCommentLike(Long commentId) {

		User findUser = userService.getLoginUser();

		Comment findComment = commentService.findVerifiedComment(commentId);

		CommentLike findCommentLike = commentLikeQueryRepository.findCommentLikeByFeedAndUser(findComment, findUser);

		if (findCommentLike == null) {
			findCommentLike = CommentLike.builder()
				.likeType(LikeType.COMMENT)
				.user(findUser)
				.comment(findComment)
				.build();

			commentLikeRepository.save(findCommentLike);

			findComment.addLike();

			if (!findUser.getId().equals(findComment.getUser().getId())) {
				String body = "작성하신 댓글 <" + findComment.getBody() + ">에 "
					+ findUser.getNickname() + "님이 좋아요를 눌렀습니다.";
				String content = findComment.getBody();
				String url = "http://localhost:8080/comments/" + findComment.getId();
				notificationService.send(findComment.getUser(), url, body, content, NotificationType.LIKE);
			}

			return findComment;
		} else {
			throw new BusinessLogicException(ExceptionCode.LIKED);
		}
	}

	@Override
	@Transactional
	public Comment deleteCommentLike(Long commentId) {

		User findUser = userService.getLoginUser();

		Comment findComment = commentService.findVerifiedComment(commentId);

		CommentLike findCommentLike = commentLikeQueryRepository.findCommentLikeByFeedAndUser(findComment, findUser);

		if (findCommentLike == null) {

			throw new BusinessLogicException(ExceptionCode.UNLIKED);
		} else {
			commentLikeRepository.delete(findCommentLike);
		}

		findComment.removeLike();

		return findComment;
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean isLikedFeed(Feed feed) {
		User findUser = userService.getLoginUser();
		return feedLikeQueryRepository.findFeedLikeByFeedAndUser(feed, findUser) != null;
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean isLikedComment(Comment comment) {
		User findUser = userService.getLoginUser();
		return commentLikeQueryRepository.findCommentLikeByFeedAndUser(comment, findUser) != null;
	}
}
