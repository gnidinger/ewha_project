package com.ewha.back.global.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.ewha.back.global.exception.ErrorResponder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {
	// Exception 발생으로 SecurityContext에 Authentication이 저장되지 않을 경우
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws ServletException, IOException {
		Exception exception = (Exception)request.getAttribute("exception");
		ErrorResponder.sendErrorResponse(response, HttpStatus.UNAUTHORIZED);

		logExceptionMessage(authException, exception);
	}

	private void logExceptionMessage(AuthenticationException authException, Exception exception) {
		String message = exception != null ? exception.getMessage() : authException.getMessage();
		log.warn("Unauthorized error happend: {}", message);
	}
}
