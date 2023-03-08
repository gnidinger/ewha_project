package com.ewha.back.global.security.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.ewha.back.global.exception.ErrorResponder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserAuthenticationFailureHandler implements AuthenticationFailureHandler {
	// 로그인 인증 실패 시
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException {
		log.error("# Authentication failed: {}", exception.getMessage());

		ErrorResponder.sendErrorResponse(response, HttpStatus.UNAUTHORIZED);
	}
}
