package com.ewha.back.global.exception;

import lombok.Getter;

public enum ExceptionCode {

    COMMENT_NOT_FOUND(404,"Comment Not Found"),
    USER_NOT_FOUND(404,"User Not Found"),
    USER_ID_EXISTS(409, "User ID Exists"),
    NICKNAME_EXISTS(409, "Nickname Exists"),
    PASSWORD_CANNOT_CHANGE(403, "Cannot Use The Same Password"),

    RATED(409, "Already Rated"),
    LIKED(409, "Already Liked"),
    UNLIKED(409, "Already Unliked"),
    FAIL_TO_LIKE(409, "Fail To Like"),

    CATEGORY_NOT_FOUND(404, "Category Not Found"),
    FEED_NOT_FOUND(404, "Feed Not Found"),
    QUESTION_NOT_FOUND(404, "Question Not Found"),
    ANSWER_NOT_FOUND(404, "Answer Not Found"),
    IMAGE_NOT_FOUND(404,"Image Not Found"),
    CHAT_ROOM_NOT_FOUND(404, "Chat Room Not Found"),

    UNAUTHORIZED(401, "Unauthorized"), // 인증이 필요한 상태
    FORBIDDEN(403, "Forbidden"), // 인증은 되었으나 권한이 없는 상태

    EMBEDDED_REDIS_EXCEPTION(500, "redis server error"),
    CAN_NOT_EXECUTE_GREP(500, "can not execute grep process command"),
    CAN_NOT_EXECUTE_REDIS_SERVER(500, "can not execute redis server"),
    NOT_FOUND_AVAILABLE_PORT(500, "not found available port");



    @Getter
    private int status;
    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
