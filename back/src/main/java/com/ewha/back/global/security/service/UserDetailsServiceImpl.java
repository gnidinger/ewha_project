package com.ewha.back.global.security.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.repository.UserRepository;
import com.ewha.back.global.exception.BusinessLogicException;
import com.ewha.back.global.exception.ExceptionCode;
import com.ewha.back.global.security.util.CustomAuthorityUtils;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;
	private final CustomAuthorityUtils authorityUtils;

	public UserDetailsServiceImpl(UserRepository userRepository, CustomAuthorityUtils authorityUtils) {
		this.userRepository = userRepository;
		this.authorityUtils = authorityUtils;
	}

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		Optional<User> optionalUser = userRepository.findByUserId(userId);
		User findUser = optionalUser.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

		return new UserDetailsImpl(findUser);
	}

	public static class UserDetailsImpl extends User implements UserDetails {
		public UserDetailsImpl(User user) {
			setId(user.getId());
			setUserId(user.getUserId());
			setIsFirstLogin(user.getIsFirstLogin());
			setPassword(user.getPassword());
			setRole(user.getRole());
			setNickname(user.getNickname());
			setAriFactor(user.getAriFactor());
			setGenderType(user.getGenderType());
			setAgeType(user.getAgeType());
			//setProviderType(user.getProviderType()); // OAuth2 반영 안함
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			//            return authorityUtils.createAuthorities(this.getRoles());
			return null; //에러 때문에 주석 처리하였습니다.
		}

		@Override
		public String getUsername() {
			return getUserId();
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}
	}
}