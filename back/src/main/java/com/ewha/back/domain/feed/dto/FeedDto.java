package com.ewha.back.domain.feed.dto;

import com.ewha.back.domain.category.dto.CategoryDto;
import com.ewha.back.domain.category.entity.CategoryType;
import com.ewha.back.domain.comment.dto.CommentDto;
import com.ewha.back.domain.user.dto.UserDto;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public class FeedDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {

        @NotEmpty(message = "내용을 입력하셔야 합니다.")
        @Size(max = 20, message = "30자를 넘을 수 없습니다.")
        private String title;

        @NotEmpty
        private List<CategoryDto.Response> categories;

        @Size(max = 1000, message = "1000자를 넘을 수 없습니다.")
        private String body;


    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Patch {

        private Long feedId;

        @NotEmpty(message = "내용을 입력하셔야 합니다.")
        @Size(max = 20, message = "30자를 넘을 수 없습니다.")
        private String title;

        @NotEmpty
        private List<CategoryDto.Response> categories;

        @Size(max = 1000, message = "1000자를 넘을 수 없습니다.")
        private String body;

        private String imagePath;

    }

    @Getter
    @Builder
    public static class Like {
        private Long likeCount;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private Long feedId;
        private List<CategoryType> categories;
        private UserDto.PostResponse userInfo;
        private String title;
        private String body;
        private Boolean isLiked;
        private Long likeCount;
        private Long viewCount;
        private String imagePath;
        private List<CommentDto.Response> comments;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListResponse {

        private Long feedId;
        private List<CategoryType> categories;
        private String userId;
        private String title;
        private Integer commentCount;
        private Long likeCount;
        private Long viewCount;
        private LocalDateTime createdAt;
    }


}
