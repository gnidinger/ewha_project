package com.ewha.back.domain.notification.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import com.ewha.back.domain.notification.dto.NotificationDto;
import com.ewha.back.domain.notification.entity.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

	default NotificationDto.Response myNotificationToResponse(Notification notification) {

		if (notification == null) {
			return null;
		}

		return  NotificationDto.Response.builder()
					.notificationId(notification.getId())
					.type(notification.getType())
					.receiverFeedTitle(notification.getReceiverFeedTitle())
					.receiverCommentBody(notification.getReceiverCommentBody())
					.isRead(true)
					.createdAt(notification.getCreatedAt())
					.build();

	}

	default List<NotificationDto.Response> myNotificationsToResponses(List<Notification> notificationList) {

		if (notificationList == null) {
			return null;
		}

		return notificationList.stream()
			.map(notification -> {
				return NotificationDto.Response.builder()
					.notificationId(notification.getId())
					.type(notification.getType())
					.receiverFeedTitle(notification.getReceiverFeedTitle())
					.receiverCommentBody(notification.getReceiverCommentBody())
					.isRead(true)
					.createdAt(notification.getCreatedAt())
					.build();
			}).collect(Collectors.toList());
	}
}
