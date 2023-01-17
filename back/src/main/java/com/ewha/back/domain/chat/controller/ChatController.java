package com.ewha.back.domain.chat.controller;

import com.ewha.back.domain.chat.dto.ChatMessageDto;
import com.ewha.back.domain.chat.dto.ChatRoomDto;
import com.ewha.back.domain.chat.entity.ChatMessage;
import com.ewha.back.domain.chat.entity.RedisChat;
import com.ewha.back.domain.chat.mapper.ChatMessageMapper;
import com.ewha.back.domain.chat.service.ChatService;
import com.ewha.back.domain.chat.service.RedisPublisher;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.service.UserService;
import com.ewha.back.global.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {
    private final UserService userService;
    private final ChatService chatService;
    private final RedisPublisher redisPublisher;
    private final ChatMessageMapper chatMessageMapper;

    /*
     * 사용자가 메시지 보내는 엔드포인트
     */
    @MessageMapping("/messages/{room_id}")
    public void message(ChatMessageDto.Request requestDto,
                        @DestinationVariable Long roomId) {

        User findUser = userService.getLoginUser();
        Long userId = findUser.getId();

        requestDto.setSenderId(userId);

        redisPublisher.publish(ChannelTopic.of("room" + roomId),
                new RedisChat(roomId, requestDto.getSenderId(), requestDto.getMessage(), LocalDateTime.now()));

        chatService.save(roomId, requestDto, LocalDateTime.now());
    }

    /*
     * 특정 채팅방 내부의 모든 대화 가져오기
     */
    @GetMapping("/messages/{room_id}")
    public ResponseEntity getAllChatsInRoom(@PathVariable("room_id") Long roomId) {

        List<ChatMessage> chatMessageList = chatService.findAllChatsInRoom(roomId);
        ChatRoomDto.Response response = chatMessageMapper.chatMessagesToResponse(chatMessageList);

        return new ResponseEntity<>(
                new SingleResponseDto<>(response), HttpStatus.OK);
    }

    /*
     * 지난 대화 모두 가져오기
     */
    @GetMapping("/rooms")
    public ResponseEntity getAllLastChats() {

        User findUser = userService.getLoginUser();
        Long userId = findUser.getId();

        List<ChatMessage> chatMessageList = chatService.findAllLastChats(userId);
        List<ChatMessageDto.LastChatResponse> responses = chatMessageMapper.lastChatsToResponses(chatMessageList, userId);

        return new ResponseEntity<>(
                new SingleResponseDto<>(responses), HttpStatus.OK);
    }
}
