package com.ewha.back.global.security.oAuth.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.ewha.back.global.exception.ErrorResponse;
import com.ewha.back.global.exception.ExceptionCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {
	// 로그인 인증 실패 시
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException {
		log.error("# Authentication failed: {}", exception.getMessage());

		ErrorResponse.of(ExceptionCode.UNAUTHORIZED);
	}
}