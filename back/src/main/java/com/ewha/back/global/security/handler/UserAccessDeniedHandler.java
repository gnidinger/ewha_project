package com.ewha.back.global.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.ewha.back.global.exception.ErrorResponder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserAccessDeniedHandler implements AccessDeniedHandler {
	// 인증에 성공했지만 해당 리소스에 대한 권한이 없을 경우
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws ServletException, IOException {
		ErrorResponder.sendErrorResponse(response, HttpStatus.FORBIDDEN);
		log.warn("Forbidden error happened: {}", accessDeniedException.getMessage());
	}
}
