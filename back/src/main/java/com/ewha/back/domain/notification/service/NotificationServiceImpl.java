package com.ewha.back.domain.notification.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ewha.back.domain.notification.controller.SseController;
import com.ewha.back.domain.notification.dto.NotificationDto;
import com.ewha.back.domain.notification.entity.Notification;
import com.ewha.back.domain.notification.entity.NotificationType;
import com.ewha.back.domain.notification.repository.EmitterRepository;
import com.ewha.back.domain.notification.repository.NotificationQueryRepository;
import com.ewha.back.domain.notification.repository.NotificationRepository;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl {
	private final EmitterRepository emitterRepository;
	private final NotificationRepository notificationRepository;
	private final NotificationQueryRepository notificationQueryRepository;
	private final UserService userService;
	private static final Long DEFAULT_TIMEOUT = Long.MAX_VALUE;

	public SseEmitter connect(String lastEventId) {

		User findUser = userService.getLoginUser();
		Long userId = findUser.getId();

		String connectId = userId + "_" + System.currentTimeMillis();

		SseEmitter sseEmitter = emitterRepository.save(connectId, new SseEmitter(DEFAULT_TIMEOUT));

		// 완료, 시간초과, 에러로 인해 요청 전송 불가시 저장해둔 sseEmitter 삭제
		sseEmitter.onCompletion(() -> emitterRepository.deleteById(connectId));
		sseEmitter.onTimeout(() -> emitterRepository.deleteById(connectId));
		sseEmitter.onError((e) -> emitterRepository.deleteById(connectId));

		sendToClient(sseEmitter, connectId, "Connected. [userId=" + userId + "]"); // 재연결 요청시 503 방지를 위한 더미 데이터

		if (!lastEventId.isEmpty()) { // 클라이언트가 미수신한 Event 유실 예방
			Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByUserId(String.valueOf(userId));
			events.entrySet().stream()
				.filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
				.forEach(entry -> sendToClient(sseEmitter, entry.getKey(), entry.getValue()));
		}

		return sseEmitter;
	}

	private void sendToClient(SseEmitter emitter, String id, Object data) {
		try {
			emitter.send(SseEmitter.event()
				.id(id)
				.name("sse")
				.data(data));
		} catch (IOException exception) {
			emitterRepository.deleteById(id);
			throw new RuntimeException("연결 오류");
		}
	}

	@Transactional
	public void send(User user, String url, String body, String content, NotificationType notificationType) {

		Notification notification = createNotification(user, url, body, content, notificationType);
		String userId = String.valueOf(user.getId());
		notificationRepository.save(notification);
		Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByUserId(userId);

		sseEmitters.forEach(
			(key, emitter) -> {
				emitterRepository.saveEventCache(key, notification);
				sendToClient(emitter, key, notification);
			}
		);
	}

	private Notification createNotification(User user, String url,
		String body, String content, NotificationType notificationType) {

		Notification notification = Notification.builder()
			.user(user)
			.type(notificationType)
			.url(url)
			.isRead(false)
			.body(body)
			.receiverBody(content)
			.build();

		return notificationRepository.save(notification);
	}

	@Transactional
	public void notifyMessagingEvent(User user) { // 메세지 알림

		User findUser = userService.getLoginUser();

		Long userId = findUser.getId();

		if (SseController.sseEmitters.containsKey(userId)) {
			SseEmitter sseEmitter = SseController.sseEmitters.get(userId);
			try {
				sseEmitter.send(SseEmitter.event().name("save")
					.data("메시지가 도착했습니다."));
			} catch (Exception e) {
				SseController.sseEmitters.remove(userId);
			}
		}

		Notification notification = Notification.builder()
			.user(findUser)
			.type(NotificationType.MESSAGE)
			.body("메시지가 도착했습니다.")
			.isRead(false)
			.build();

		notificationRepository.save(notification);
	}

	@Transactional
	public Notification getMyNotification(Long notificationId) {

		User findUser = userService.getLoginUser();
		Long userId = findUser.getId();

		return notificationQueryRepository.getMyNotification(userId, notificationId);
	}

	@Transactional
	public List<Notification> getMyNotifications() {

		User findUser = userService.getLoginUser();
		Long userId = findUser.getId();

		List<Notification> response =  notificationQueryRepository.getMyNotifications(userId);

		response.forEach(notification -> notification.setRead(true));

		return response;
	}

	@Transactional
	public Boolean findIfNotReadNotifications() {

		User findUser = userService.getLoginUser();
		Long userId = findUser.getId();

		return notificationQueryRepository.findIfNotReadNotifications(userId);
	}

	@Transactional
	public void deleteNotification(Long notificationId) {

		User findUser = userService.getLoginUser();

		notificationQueryRepository.deleteNotifications(notificationId);

	}

	@Transactional
	public void deleteAllMyNotifications() {

		User findUser = userService.getLoginUser();
		Long userId = findUser.getId();

		notificationQueryRepository.deleteAllMyNotifications(userId);
	}
}
