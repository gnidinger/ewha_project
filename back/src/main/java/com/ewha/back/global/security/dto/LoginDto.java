package com.ewha.back.global.security.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ewha.back.domain.category.entity.CategoryType;
import com.ewha.back.domain.user.entity.enums.AgeType;
import com.ewha.back.domain.user.entity.enums.GenderType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class LoginDto {

	@Getter
	@Setter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class PostDto { // 일반 로그인 request
		@NotBlank(message = "아이디를 입력하셔야 합니다")
		private String userId;
		@NotBlank(message = "패스워드를 입력하셔야 합니다")
		private String password;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PatchDto { // 첫 로그인 request
		@NotNull
		private GenderType genderType;
		@NotNull
		private AgeType ageType;
		@NotEmpty
		@Size(max = 3, message = "장르는 최대 3개까지 선택 가능합니다")
		private List<CategoryType> categories;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ResponseDto { // 일반 로그인 response

		private Long id;
		private String userId;
		private Boolean isFirstLogin;
		private String nickname;
		private Double ariFactor;
		private List<String> role;
		private String profileImage; // 프로필 이미지
	}

	@Setter
	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class FirstLoginResponseDto { // 첫 로그인 response
		private Boolean firstLogin;
		private String nickName;
		private Double ariFactor;
		private List<String> roles;
		private GenderType genderType;
		private AgeType ageType;
		private List<CategoryType> categoryTypes;
		private String profileImage; // 프로필 이미지
	}
}
