package com.ewha.back.domain.feed.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.ewha.back.domain.category.dto.CategoryDto;
import com.ewha.back.domain.category.entity.CategoryType;
import com.ewha.back.domain.comment.dto.CommentDto;
import com.ewha.back.domain.user.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class FeedDto {

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Post {

		@NotEmpty(message = "내용을 입력하셔야 합니다.")
		@Size(max = 20, message = "30자를 넘을 수 없습니다.")
		private String title;

		@NotEmpty
		private List<CategoryDto.Response> categories;

		@Size(max = 1000, message = "1000자를 넘을 수 없습니다.")
		private String body;

	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Patch {

		private Long feedId;

		@NotEmpty(message = "내용을 입력하셔야 합니다.")
		@Size(max = 20, message = "30자를 넘을 수 없습니다.")
		private String title;

		@NotEmpty
		private List<CategoryDto.Response> categories;

		@Size(max = 1000, message = "1000자를 넘을 수 없습니다.")
		private String body;

		private String imagePath;

		private String thumbnailPath;

	}

	@Getter
	@Builder
	public static class Like {
		private Long likeCount;

	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Response {

		private Long feedId;
		private List<CategoryType> categories;
		private UserDto.PostResponse userInfo;
		private String title;
		private String body;
		private Boolean isLiked;
		private Boolean isMyFeed;
		private Long likeCount;
		private Long viewCount;
		private String imagePath;
		private String thumbnailPath;
		private List<CommentDto.FeedCommentResponse> comments;
		private LocalDateTime createdAt;
		private LocalDateTime modifiedAt;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ListResponse {

		private Long feedId;
		private List<CategoryType> categories;
		private String body;
		private String userId;
		private String title;
		private Integer commentCount;
		private Long likeCount;
		private Long viewCount;
		private LocalDateTime createdAt;
	}

}
