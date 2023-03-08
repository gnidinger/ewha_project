package com.ewha.back.domain.chat.service;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.ewha.back.domain.chat.entity.RedisChat;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {
	private final ObjectMapper objectMapper;
	private final RedisTemplate redisTemplate;
	private final SimpMessageSendingOperations simpMessageSendingOperations;

	@Override
	public void onMessage(Message message, byte[] pattern) {

		try {
			String publishMessage = String.valueOf(redisTemplate.getStringSerializer().deserialize(message.getBody()));
			RedisChat redisChat = objectMapper.readValue(publishMessage, RedisChat.class);
			simpMessageSendingOperations.convertAndSend("/sub/room" + redisChat.getRoomId(), redisChat);
			log.info("Received From Redis");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
