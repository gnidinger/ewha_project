package com.ewha.back.domain.comment.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.ewha.back.domain.user.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CommentDto {

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Post {

		@NotBlank(message = "내용을 입력하셔야 합니다.")
		@Size(min = 5, max = 500, message = "5자 이상 입력하셔야 합니다.")
		private String body;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Patch {

		@NotBlank(message = "내용을 입력하셔야 합니다.")
		@Size(min = 5, max = 500, message = "5자 이상 입력하셔야 합니다.")
		private String body;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Response {

		private Long commentId;
		private Long feedId;
		private UserDto.PostResponse userInfo;
		private String body;
		private Long likeCount;
		private LocalDateTime createdAt;
		private LocalDateTime modifiedAt;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class FeedCommentResponse {

		private Long commentId;
		private Long feedId;
		private Boolean isLikedComment;
		private UserDto.FeedCommentResponse userInfo;
		private String body;
		private Long likeCount;
		private LocalDateTime createdAt;
		private LocalDateTime modifiedAt;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ListResponse {

		private Long commentId;
		private Long feedId;
		private String body;
		private Long likeCount;
		private LocalDateTime createdAt;
	}
}
