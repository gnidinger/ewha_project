package com.ewha.back.global.security.event;

import org.springframework.context.ApplicationEvent;

import com.ewha.back.domain.user.entity.User;

import lombok.Getter;

@Getter
public class UserRegistrationApplicationEvent extends ApplicationEvent {
	private User user;

	public UserRegistrationApplicationEvent(Object source, User user) {
		super(source);
		this.user = user;
	}
}
