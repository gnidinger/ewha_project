package com.ewha.back.domain.notification.repository;

import static com.ewha.back.domain.notification.entity.QNotification.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.ewha.back.domain.notification.entity.Notification;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NotificationQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final EntityManager entityManager;

	public Notification getMyNotification(Long userId, Long notificationId) {

		return jpaQueryFactory
			.selectFrom(notification)
			.where(notification.user.id.eq(userId).and(notification.id.eq(notificationId)))
			.fetchOne();
	}

	public List<Notification> getMyNotifications(Long userId) {

		return jpaQueryFactory
			.selectFrom(notification)
			.where(notification.user.id.eq(userId))
			.fetch();
	}

	public void deleteNotifications(Long notificationId) {

		jpaQueryFactory
			.delete(notification)
			.where(notification.id.eq(notificationId))
			.execute();
	}

	public void deleteAllMyNotifications(Long userId) {

		jpaQueryFactory
			.delete(notification)
			.where(notification.user.id.eq(userId))
			.execute();
	}
}
