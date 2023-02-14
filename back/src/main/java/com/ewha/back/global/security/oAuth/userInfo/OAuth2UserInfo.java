package com.ewha.back.global.security.oAuth.userInfo;

import java.util.Map;

public interface OAuth2UserInfo {
	Map<String, Object> getAttributes();

	String getProviderId();

	String getProvider();

	String getEmail();

	String getName();
}
