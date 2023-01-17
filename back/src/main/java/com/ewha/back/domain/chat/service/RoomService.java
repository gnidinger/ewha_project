package com.ewha.back.domain.chat.service;

import com.ewha.back.domain.chat.dto.ChatMessageDto;
import com.ewha.back.domain.chat.entity.ChatRoom;
import com.ewha.back.domain.chat.repository.ChatQueryRepository;
import com.ewha.back.domain.chat.repository.ChatRoomRepository;
import com.ewha.back.domain.user.service.UserService;
import com.ewha.back.global.exception.BusinessLogicException;
import com.ewha.back.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatQueryRepository chatQueryRepository;
    private final UserService userService;
    private HashOperations<String, String, ChatRoom> roomHashOperations;
    private final RedisTemplate<String, Object> redisTemplate;
    private final Map<String, ChannelTopic> topics;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisSubscriber redisSubscriber;


    @PostConstruct
    private void init() {
        roomHashOperations = redisTemplate.opsForHash();
    }

    private ChatRoom getOrCreate(ChatMessageDto.Request request) {

        ChatRoom chatRoom = chatQueryRepository.getOrCreate(request.getSenderId(), request.getReceiverId())
                .orElseGet(() ->
                        ChatRoom.create(userService.findVerifiedUser(request.getSenderId()),
                                userService.findVerifiedUser(request.getReceiverId())));

        String roomId = "room" + chatRoom.getId(); // 새로운 토픽 생성

        if (!topics.containsKey(roomId)) {

            ChannelTopic channelTopic = new ChannelTopic(roomId);
            redisMessageListenerContainer.addMessageListener(redisSubscriber, channelTopic);
            topics.put(roomId, channelTopic); // 토픽 주입
        }

        return chatRoomRepository.save(chatRoom);
    }

    @Transactional(readOnly = true)
    public List<ChatRoom> findAllMyRooms(Long userId) {
        return chatRoomRepository.findBySenderIdOrReceiverId(userId, userId);
    }

    @Transactional(readOnly = true)
    public ChatRoom findById(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CHAT_ROOM_NOT_FOUND));
    }

}
