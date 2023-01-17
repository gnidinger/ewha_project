package com.ewha.back.domain.chat.repository;

import com.ewha.back.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    List<ChatRoom> findBySenderIdOrReceiverId(Long senderId, Long receiverId);
}
