package com.ewha.back.global.smsAuth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class SmsDto {

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class NumberRequest {
		private String phoneNumber;
		private String certificationNumber;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CertificationRequest {
		private String phoneNumber;
		private String certificationNumber;
	}
}
