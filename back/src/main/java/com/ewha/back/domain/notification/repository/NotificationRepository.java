package com.ewha.back.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ewha.back.domain.notification.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
