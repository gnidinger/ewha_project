package com.ewha.back.domain.like.service;

import com.ewha.back.domain.comment.entity.Comment;
import com.ewha.back.domain.feed.entity.Feed;

public interface LikeService {
	Feed createFeedLike(Long feedId);

	Feed deleteFeedLike(Long feedId);

	Comment createCommentLike(Long commentId);

	Comment deleteCommentLike(Long commentId);

	Boolean isLikedFeed(Feed feed);

	Boolean isLikedComment(Comment comment);
}
