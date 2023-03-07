package com.ewha.back.domain.comment.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ewha.back.domain.comment.entity.Comment;
import com.ewha.back.domain.comment.repository.CommentQueryRepository;
import com.ewha.back.domain.comment.repository.CommentRepository;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.feed.service.FeedService;
import com.ewha.back.domain.notification.entity.NotificationType;
import com.ewha.back.domain.notification.service.NotificationServiceImpl;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.service.UserService;
import com.ewha.back.global.exception.BusinessLogicException;
import com.ewha.back.global.exception.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final UserService userService;
	private final FeedService feedService;
	private final CommentRepository commentRepository;
	private final CommentQueryRepository commentQueryRepository;
	private final NotificationServiceImpl notificationService;

	@Override
	public Comment createComment(Comment comment, Long feedId) {

		User findUser = userService.getLoginUser();

		Feed findFeed = feedService.findVerifiedFeed(feedId);

		Comment savedComment =
			Comment.builder()
				.feed(findFeed)
				.user(findUser)
				.body(comment.getBody())
				.likeCount(0L)
				.build();

		if (!findUser.getId().equals(findFeed.getUser().getId())) {
			String body = "작성하신 피드 <" + findFeed.getTitle() + ">에 "
				+ findUser.getNickname() + "님이 댓글을 남겼습니다.";
			String content = findFeed.getTitle();
			String url = "http://localhost:8080/feeds/" + findFeed.getId();
			notificationService.send(findFeed.getUser(), url, body, content, NotificationType.COMMENT);
		}

		return commentRepository.save(savedComment);
	}

	@Override
	public Comment updateComment(Comment comment, Long commentId) {

		User findUser = userService.getLoginUser();

		Comment findComment = findVerifiedComment(commentId);

		if (findUser.equals(findComment.getUser())) {

			findComment.updateComment(comment);

			return commentRepository.save(findComment);
		} else {
			throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
		}
	}

	@Override
	public Page<Comment> getFeedComments(Long feedId, int page) {

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return commentQueryRepository.findFeedComment(feedId, pageRequest);

	}

	@Override
	public List<Comment> isMyComments(Long feedId) {

		User findUser = userService.getLoginUser();

		Feed findFeed = feedService.findFeedByFeedId(feedId);

		return findFeed.getComments().stream()
			.filter(comment -> comment.getUser().equals(findUser))
			.collect(Collectors.toList());
	}

	@Override
	public void deleteComment(Long commentId) {

		User findUser = userService.getLoginUser();

		Comment findComment = findVerifiedComment(commentId);

		if (findUser.equals(findComment.getUser())) {
			commentRepository.delete(findComment);
		} else {
			throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
		}
	}

	@Override
	public void deleteComments(Long commentId) {

		User findUser = userService.getLoginUser();

		Long id = findUser.getId();

		commentRepository.deleteAllByUserId(id);

		Comment findComment = findVerifiedComment(commentId);
	}

	@Override
	public Comment findVerifiedComment(long commentId) {

		Optional<Comment> optionalComment = commentRepository.findById(commentId);
		return optionalComment.orElseThrow(() ->
			new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));
	}

}
