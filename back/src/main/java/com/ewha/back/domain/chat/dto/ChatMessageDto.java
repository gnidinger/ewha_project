package com.ewha.back.domain.chat.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ChatMessageDto {

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Request {
		@NotNull
		private Long senderId;
		@NotNull
		private Long receiverId;
		@NotNull
		private String message;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Response {
		private Long userId;
		private String nickname;
		private String profileImage;
		private String message;
		private LocalDateTime createdAt;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class LastChatResponse {
		private Long roomId;
		private Long senderId;
		private Long receiverId;
		private String mateNickname;
		private String lastMessage;
		private String mateProfileImage;
		private LocalDateTime createAt;
	}
}
