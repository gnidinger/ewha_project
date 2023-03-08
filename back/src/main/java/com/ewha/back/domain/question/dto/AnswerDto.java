package com.ewha.back.domain.question.dto;

import com.ewha.back.domain.question.entity.Question;
import com.ewha.back.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AnswerDto {

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Post {
		private String body;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Response {
		private Long answerId;
		private User user;
		private Question question;
		private String body;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MyAnswerResponse {
		private Long answerId;
		private User user;
		private Question question;
		private String body;
	}
}
