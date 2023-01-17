package com.ewha.back.domain.chat.service;

import com.ewha.back.domain.chat.dto.ChatMessageDto;
import com.ewha.back.domain.chat.entity.ChatMessage;
import com.ewha.back.domain.chat.entity.ChatRoom;
import com.ewha.back.domain.chat.repository.ChatQueryRepository;
import com.ewha.back.domain.notification.service.NotificationService;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {
    private final UserService userService;
    private final RoomService roomService;
    private final ChatQueryRepository chatQueryRepository;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public List<ChatMessage> findAllChatsInRoom(Long roomId) {

        User findUser = userService.getLoginUser();

        ChatRoom chatRoom = roomService.findById(roomId);
        List<ChatMessage> chatMessageList = chatQueryRepository.findAllChatsInRoom(roomId);

        return chatMessageList;
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> findAllLastChats(Long userId) {

        List<ChatRoom> chatRoomList = roomService.findAllMyRooms(userId);
        List<ChatMessage> chatMessageList = chatQueryRepository.findAllChats(chatRoomList);

        return chatMessageList;
    }

    @Transactional
    public void save(Long roomId, ChatMessageDto.Request requestDto, LocalDateTime now) {

        User sender = userService.findVerifiedUser(requestDto.getSenderId());
        User receiver = userService.findVerifiedUser(requestDto.getReceiverId());
        ChatRoom chatRoom = roomService.findById(roomId);

        ChatMessage chatMessage = ChatMessage.builder()
                .sender(sender)
                .receiver(receiver)
                .message(requestDto.getMessage())
                .room(chatRoom)
                .createdAt(now)
                .build();

        chatQueryRepository.save(chatMessage);

        notificationService.notifyMessagingEvent(receiver);
    }
}
