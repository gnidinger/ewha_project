package com.ewha.back.domain.notification.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ewha.back.domain.notification.entity.Notification;
import com.ewha.back.domain.notification.entity.NotificationType;
import com.ewha.back.domain.user.entity.User;

public interface NotificationService {
	SseEmitter connect(String lastEventId);

	@Transactional
	void send(User user, String url, String body, String content, NotificationType notificationType);

	@Transactional
	void notifyMessagingEvent(User user);

	@Transactional
	Notification getMyNotification(Long notificationId);

	@Transactional
	List<Notification> getMyNotifications();

	@Transactional
	Boolean findIfNotReadNotifications();

	@Transactional
	void deleteNotification(Long notificationId);

	@Transactional
	void deleteAllMyNotifications();
}
