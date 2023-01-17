package com.ewha.back.domain.notification.dto;

import lombok.*;

import java.time.LocalDateTime;

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
