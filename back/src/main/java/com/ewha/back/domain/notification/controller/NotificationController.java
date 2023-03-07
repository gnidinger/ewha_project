package com.ewha.back.domain.notification.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ewha.back.domain.notification.dto.NotificationDto;
import com.ewha.back.domain.notification.entity.Notification;
import com.ewha.back.domain.notification.mapper.NotificationMapper;
import com.ewha.back.domain.notification.service.NotificationServiceImpl;
import com.ewha.back.global.dto.ListResponseDto;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
	private final NotificationServiceImpl notificationService;
	private final NotificationMapper notificationMapper;

	@GetMapping
	public ResponseEntity getMyNotifications() {

		List<Notification> notificationList = notificationService.getMyNotifications();
		List<NotificationDto.Response> responses = notificationMapper.myNotificationsToResponses(notificationList);

		return new ResponseEntity<>(
			new ListResponseDto<>(responses), HttpStatus.OK);
	}

	@GetMapping("/check")
	public ResponseEntity findIfNotReadNotifications() {

		Boolean response = notificationService.findIfNotReadNotifications();

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/{notification_id}")
	public ResponseEntity getMyNotification(@PathVariable("notification_id") Long notificationId) {

		Notification notification = notificationService.getMyNotification(notificationId);
		NotificationDto.Response response = notificationMapper.myNotificationToResponse(notification);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/{notification_id}/delete")
	public void deleteNotification(@PathVariable("notification_id") Long notificationId) {
		notificationService.deleteNotification(notificationId);
	}

	@DeleteMapping("/delete")
	public void deleteAllMyNotifications() {
		notificationService.deleteAllMyNotifications();
	}
}
