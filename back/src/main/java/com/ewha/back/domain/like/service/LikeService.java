package com.ewha.back.domain.like.service;

import com.ewha.back.domain.comment.entity.Comment;
import com.ewha.back.domain.comment.service.CommentService;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.feed.service.FeedService;
import com.ewha.back.domain.like.entity.Like;
import com.ewha.back.domain.like.entity.LikeType;
import com.ewha.back.domain.like.repository.LikeRepository;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.service.UserService;
import com.ewha.back.global.exception.BusinessLogicException;
import com.ewha.back.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {
    private final UserService userService;
    private final FeedService feedService;
    private final CommentService commentService;
    private final LikeRepository likeRepository;

    public Feed createFeedLike(Long feedId) {

        User findUser = userService.getLoginUser();

        Feed findFeed = feedService.findVerifiedFeed(feedId);

        Like findFeedLike = likeRepository.findByFeedAndUser(findFeed, findUser);

        if (findFeedLike == null) {
            findFeedLike = Like.builder()
                    .likeType(LikeType.FEED)
                    .user(findUser)
                    .feed(findFeed)
                    .build();

            likeRepository.save(findFeedLike);

            findFeed.addLike();

            return findFeed;
        }

        else throw new BusinessLogicException(ExceptionCode.LIKED);
    }

    public Feed deleteFeedLike(Long feedId) {

        User findUser = userService.getLoginUser();

        Feed findFeed = feedService.findVerifiedFeed(feedId);

        Like findFeedLike = likeRepository.findByFeedAndUser(findFeed, findUser);

        if (findFeedLike == null) {

            throw new BusinessLogicException(ExceptionCode.UNLIKED);
        }

        else likeRepository.delete(findFeedLike);

        findFeed.removeLike();

        return findFeed;
    }

    public Comment createCommentLike(Long commentId) {

        User findUser = userService.getLoginUser();

        Comment findComment = commentService.findVerifiedComment(commentId);

        Like findCommentLike = likeRepository.findByCommentAndUser(findComment, findUser);

        if (findCommentLike == null) {
            findCommentLike = Like.builder()
                    .likeType(LikeType.COMMENT)
                    .user(findUser)
                    .comment(findComment)
                    .build();

            likeRepository.save(findCommentLike);

            findComment.addLike();

            return findComment;
        }

        else throw new BusinessLogicException(ExceptionCode.LIKED);
    }

    public Comment deleteCommentLike(Long commentId) {

        User findUser = userService.getLoginUser();

        Comment findComment = commentService.findVerifiedComment(commentId);

        Like findCommentLike = likeRepository.findByCommentAndUser(findComment, findUser);

        if (findCommentLike == null) {

            throw new BusinessLogicException(ExceptionCode.UNLIKED);
        }

        else likeRepository.delete(findCommentLike);

        findComment.removeLike();

        return findComment;
    }
}
