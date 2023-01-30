package com.ewha.back.domain.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class QuestionDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        @NotBlank
        private String title;
        @NotBlank
        private String body;
        private String imagePath;
        @NotBlank
        private String answerBody;
        @NotBlank
        private String dummy1;
        @NotBlank
        private String dummy2;
        @NotBlank
        private String dummy3;
        @NotBlank
        private String dummy4;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Patch {
        @NotBlank
        private String title;
        @NotBlank
        private String body;
        private String imagePath;
        private String thumbnailPath;
        @NotBlank
        private String answerBody;
        @NotBlank
        private String dummy1;
        @NotBlank
        private String dummy2;
        @NotBlank
        private String dummy3;
        @NotBlank
        private String dummy4;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long questionId;
        private String title;
        private String body;
        private String imagePath;
        private String answerBody;
        private String dummy1;
        private String dummy2;
        private String dummy3;
        private String dummy4;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnsweredResponse {
        private Long questionId;
        private String title;
        private String body;
        private String imagePath;
        private String thumbnailPath;
        private String answerBody;
        private String userAnswer;
    }
}
