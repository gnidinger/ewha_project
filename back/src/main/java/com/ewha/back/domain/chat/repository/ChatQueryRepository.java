package com.ewha.back.domain.chat.repository;

import static com.ewha.back.domain.chat.entity.QChatMessage.*;
import static com.ewha.back.domain.chat.entity.QChatRoom.*;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.ewha.back.domain.chat.entity.ChatMessage;
import com.ewha.back.domain.chat.entity.ChatRoom;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;
	private final EntityManager entityManager;

	public List<ChatMessage> findAllChats(List<ChatRoom> rooms) {
		return jpaQueryFactory
			.selectFrom(chatMessage)
			.where(Expressions.list(chatMessage.room, chatMessage.createdAt)
				.in((JPAExpressions
					.select(chatMessage.room, chatMessage.createdAt.max())
					.from(chatMessage)
					.where(chatMessage.room.in(rooms))
					.groupBy(chatMessage.room))))
			.orderBy(chatMessage.createdAt.desc())
			.fetch();
	}

	public Optional<ChatRoom> getOrCreate(Long senderId, Long receiverId) {
		return Optional.ofNullable(jpaQueryFactory
			.selectFrom(chatRoom)
			.where(chatRoom.sender.id.eq(senderId)
				.and(chatRoom.receiver.id.eq(receiverId)))
			.fetchFirst());
	}

	public void save(ChatMessage message) {
		entityManager.persist(message);
	}

	public List<ChatMessage> findAllChatsInRoom(Long roomId) {
		return jpaQueryFactory
			.select(chatMessage)
			.from(chatMessage)
			.where(chatMessage.room.id.eq(roomId))
			.fetch();
	}
}
