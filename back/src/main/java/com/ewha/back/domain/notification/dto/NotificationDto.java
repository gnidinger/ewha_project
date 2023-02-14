package com.ewha.back.domain.notification.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class NotificationDto {

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Response {
		private Long notificationId;
		private String receiverFeedTitle;
		private String receiverCommentBody;
		private Boolean isRead;
		private LocalDateTime createdAt;
	}
}
