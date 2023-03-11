package com.ewha.back.Controller.constant;

import static com.ewha.back.Controller.constant.FeedControllerConstant.*;
import static com.ewha.back.Controller.constant.UserControllerConstant.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageImpl;

import com.ewha.back.domain.comment.dto.CommentDto;
import com.ewha.back.domain.user.dto.UserDto;

public class CommentControllerConstant {

	public static final CommentDto.Post POST_COMMENT_DTO =
		CommentDto.Post.builder()
			.body("댓글 내용")
			.build();

	public static final CommentDto.Response COMMENT_RESPONSE_DTO =
		CommentDto.Response.builder()
			.commentId(1L)
			.feedId(1L)
			.userInfo(USER_BASIC_RESPONSE)
			.body("댓글 내용")
			.likeCount(1L)
			.createdAt(LocalDateTime.now())
			.modifiedAt(LocalDateTime.now())
			.build();

	public static final CommentDto.Patch PATCH_COMMENT_DTO =
		CommentDto.Patch.builder()
			.body("댓글 내용")
			.build();

	public static final PageImpl<CommentDto.Response> COMMENT_RESPONSE_PAGE =
		new PageImpl<>(List.of(COMMENT_RESPONSE_DTO, COMMENT_RESPONSE_DTO));
}
