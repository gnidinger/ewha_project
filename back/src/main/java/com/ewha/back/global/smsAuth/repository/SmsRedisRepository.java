package com.ewha.back.global.smsAuth.repository;

import static com.ewha.back.global.smsAuth.controller.SmsConstant.*;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SmsRedisRepository {

	private final StringRedisTemplate stringRedisTemplate;

	public void createCertification(String phone, String certificationNumber) { // 요청번호, 인증번호를 수명과 함께 저장
		stringRedisTemplate.opsForValue()
			.set(PREFIX + phone, certificationNumber, Duration.ofSeconds(LIMIT_TIME));
	}

	public String getCertification(String phone) { // 요청번호 리턴
		return stringRedisTemplate.opsForValue().get(PREFIX + phone);
	}

	public void removeCertification(String phone) { // 인증 완료 후 인증번호 삭제
		stringRedisTemplate.delete(PREFIX + phone);
	}

	public boolean hasKey(String phone) {  // 요청번호(KEY)로 저장된 인증번호(VALUE)가 있는지 확인
		return stringRedisTemplate.hasKey(PREFIX + phone);
	}
}
