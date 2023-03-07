package com.ewha.back.domain.notification.dto;

import java.time.LocalDateTime;

import com.ewha.back.domain.notification.entity.NotificationType;

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
		private NotificationType type;
		// private String receiverFeedTitle;
		// private String receiverCommentBody;
		private String receiverBody;
		private Boolean isRead;
		private LocalDateTime createdAt;
	}
}
