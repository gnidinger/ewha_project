package com.ewha.back.domain.notification.repository;

import java.util.Map;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository {

	SseEmitter save(String emitterId, SseEmitter sseEmitter); // emitter 저장

	void saveEventCache(String emitterId, Object event); // event 저장

	Map<String, SseEmitter> findAllEmitterStartWithByUserId(String userId); // 해당 회원과 관련된 모든 emitter 검색

	Map<String, Object> findAllEventCacheStartWithByUserId(String userId); // 해당 회원과 관련된 모든 event 검색

	void deleteById(String id); // emitter 하나 삭제

	void deleteAllEmitterStartWithId(String userId); // 해당 회원과 관련된 모든 emitter 삭제

	void deleteAllEventCacheStartWithId(String userId); // 해당 회원과 관련된 모든 event 삭제
}
