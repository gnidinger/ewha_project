package com.ewha.back.domain.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class QuestionDto {

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
        private String answerBody;
        private String userAnswer;
    }
}
