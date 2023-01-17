package com.ewha.back.domain.chat.dto;

import lombok.*;

import java.util.List;

public class ChatRoomDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    public static class Post {

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private List<ChatRoomDto.UserResponse> users;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResponse {

        public Long userId;
        public String profileImage;
        public String nickName;
    }
}
