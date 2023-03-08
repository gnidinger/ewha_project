package com.ewha.back.domain.chat.mapper;

import com.ewha.back.domain.chat.dto.ChatMessageDto;
import com.ewha.back.domain.chat.dto.ChatRoomDto;
import com.ewha.back.domain.chat.entity.ChatMessage;
import com.ewha.back.domain.chat.entity.ChatRoom;

import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {

	default ChatRoomDto.Response chatMessagesToResponse(List<ChatMessage> chatMessageList) {

		ChatRoom chatRoom = chatMessageList.stream().map(ChatMessage::getRoom).findFirst().get();

		List<ChatMessageDto.Response> responseList = chatMessageList.stream()
			.map(chatMessage -> {
				return new ChatMessageDto.Response(chatMessage.getSender().getId(),
					chatMessage.getSender().getNickname(), chatMessage.getSender().getProfileImage(),
					chatMessage.getMessage(), chatMessage.getCreatedAt());
			}).collect(Collectors.toList());

		return ChatRoomDto.Response.builder()
			.users(List.of(
				ChatRoomDto.UserResponse.builder()
					.userId(chatRoom.getSender().getId())
					.profileImage(chatRoom.getSender().getProfileImage())
					.nickName(chatRoom.getSender().getNickname())
					.build(),
				ChatRoomDto.UserResponse.builder()
					.userId(chatRoom.getReceiver().getId())
					.profileImage(chatRoom.getReceiver().getProfileImage())
					.nickName(chatRoom.getReceiver().getNickname())
					.build()
			))
			.build();
	}

	default List<ChatMessageDto.LastChatResponse> lastChatsToResponses(List<ChatMessage> chatMessageList, Long userId) {

		return chatMessageList.stream()
			.map(chatMessage -> {
				return ChatMessageDto.LastChatResponse.builder()
					.roomId(chatMessage.getRoom().getId())
					.senderId(chatMessage.getRoom().getSender().getId())
					.receiverId(chatMessage.getRoom().getReceiver().getId())
					.mateNickname(chatMessage.getRoom().mate(userId).getNickname())
					.mateProfileImage(chatMessage.getRoom().mate(userId).getProfileImage())
					.lastMessage(chatMessage.getMessage())
					.createAt(chatMessage.getCreatedAt())
					.build();
			})
			.collect(Collectors.toList());
	}
}
