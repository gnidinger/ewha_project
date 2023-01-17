package com.ewha.back.global.exception;

import lombok.Getter;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public enum ExceptionCode {

    NOT_FOUND(404, "Not Found"),

    BOOK_NOT_FOUND(404,"Book Not Found"),

    BOOK_WIKI_NOT_FOUND(404,"Book Wiki Not Found"),
    BOOK_WIKI_EXISTS(409, "Book Wiki Already Exists"),

    IMAGE_NOT_FOUND(404,"Image Not Found"),

    PAIRING_NOT_FOUND(404,"Pairing Not Found"),
    COLLECTION_NOT_FOUND(404,"Collection Not Found"),
    COMMENT_NOT_FOUND(404,"Comment Not Found"),
    COMMENT_EXISTS(409, "Comment Exists"),
    COMMENT_CANNOT_CHANGE(403,"Comment Can Not Be Changed"),

    ANSWER_DELETED(405, "Answer Already Deleted"),

    USER_NOT_FOUND(404,"User Not Found"),
    USER_ID_EXISTS(409, "User ID Exists"),
    NICKNAME_EXISTS(409, "Nickname Exists"),
    PASSWORD_CANNOT_CHANGE(403, "Cannot Use The Same Password"),

    RATED(409, "Already Rated"),
    LIKED(409, "Already Liked"),
    UNLIKED(409, "Already Unliked"),
    FAIL_TO_LIKE(409, "Fail To Like"),
    FAIL_TO_BOOKMARK(409, "Fail To Bookmark"),


    CATEGORY_NOT_FOUND(404, "Category Not Found"),
    FEED_NOT_FOUND(404, "Feed Not Found"),
    QUESTION_NOT_FOUND(404, "Question Not Found"),
    ANSWER_NOT_FOUND(404, "Answer Not Found"),
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
