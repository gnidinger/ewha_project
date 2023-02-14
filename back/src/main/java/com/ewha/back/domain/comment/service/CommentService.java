package com.ewha.back.domain.comment.service;

import org.springframework.data.domain.Page;

import com.ewha.back.domain.comment.entity.Comment;

public interface CommentService {
	Comment createComment(Comment comment, Long feedId);

	Comment updateComment(Comment comment, Long commentId);

	Page<Comment> getFeedComments(Long feedId, int page);

	void deleteComment(Long commentId);

	void deleteComments(Long commentId);

	Comment findVerifiedComment(long commentId);
}
