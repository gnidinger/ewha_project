package com.ewha.back.domain.comment.service;

import com.ewha.back.domain.comment.entity.Comment;
import com.ewha.back.domain.comment.repository.CommentQueryRepository;
import com.ewha.back.domain.comment.repository.CommentRepository;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.feed.service.FeedService;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.service.UserService;
import com.ewha.back.global.exception.BusinessLogicException;
import com.ewha.back.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final UserService userService;
    private final FeedService feedService;
    private final CommentRepository commentRepository;
    private final CommentQueryRepository commentQueryRepository;

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

        return commentRepository.save(savedComment);
    }

    public Comment updateComment(Comment comment, Long commentId) {

        User findUser = userService.getLoginUser();

        Comment findComment = findVerifiedComment(commentId);

        if (findUser.equals(findComment.getUser())) {

            findComment.updateComment(comment);

            return commentRepository.save(findComment);
        }
        else throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
    }

    public Page<Comment> getFeedComments(Long feedId, int page) {

        PageRequest pageRequest = PageRequest.of(page - 1, 10);

        return commentQueryRepository.findFeedComment(feedId, pageRequest);

    }

    public Comment addLike(Long commentId) {
        return null;
    }

    public Comment removeLike(Long commentId) {
        return null;
    }

    public Comment isLikedComment(Long commentId) {
        return null;
    }

    public void deleteComment(Long commentId) {

        User findUser = userService.getLoginUser();

        Comment findComment = findVerifiedComment(commentId);

        if (findUser.equals(findComment.getUser())) {
            commentRepository.delete(findComment);
        }
        else throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
    }

    public void deleteComments(Long commentId) {

        User findUser = userService.getLoginUser();

        Long id = findUser.getId();

        commentRepository.deleteAllByUserId(id);

        Comment findComment = findVerifiedComment(commentId);
    }

    public Comment findVerifiedComment(long commentId) {

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        return optionalComment.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));
    }

}
