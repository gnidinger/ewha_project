package com.ewha.back.domain.question.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class QuestionDto {

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Post {
		@NotBlank
		private String title;
		@NotBlank
		private String body;
		private String imagePath;
		@NotBlank
		private String answerBody;

	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Patch {
		@NotBlank
		private String title;
		@NotBlank
		private String body;
		private String imagePath;
		private String thumbnailPath;
		@NotBlank
		private String answerBody;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Response {
		private Long questionId;
		private String title;
		private String body;
		private String imagePath;
		private String answerBody;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AnsweredResponse {
		private Long questionId;
		private String title;
		private String body;
		private String imagePath;
		private String thumbnailPath;
		private String answerBody;
		private String userAnswer;
	}
}
