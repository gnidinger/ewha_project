package com.ewha.back.domain.notification.controller;

import com.ewha.back.domain.notification.dto.NotificationDto;
import com.ewha.back.domain.notification.entity.Notification;
import com.ewha.back.domain.notification.mapper.NotificationMapper;
import com.ewha.back.domain.notification.service.NotificationService;
import com.ewha.back.global.dto.ListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    @GetMapping
    public ResponseEntity getMyNotifications() {

        List<Notification> notificationList = notificationService.getMyNotifications();
        List<NotificationDto.Response> responses = notificationMapper.myNotificationsToResponses(notificationList);

        return new ResponseEntity<>(
                new ListResponseDto<>(responses), HttpStatus.OK);
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
