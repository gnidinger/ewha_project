package com.ewha.back.global.smsAuth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.nurigo.java_sdk.exceptions.CoolsmsException;

import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.service.UserService;
import com.ewha.back.global.smsAuth.dto.SmsDto;
import com.ewha.back.global.smsAuth.service.SmsService;

import lombok.RequiredArgsConstructor;

@RequestMapping
@RestController
@RequiredArgsConstructor
public class SmsController {
	private final SmsService smsService;
	private final UserService userService;

	@PostMapping("/sms/send")
	public ResponseEntity<Void> smsCertification(@RequestBody SmsDto.NumberRequest request) throws CoolsmsException {

		System.out.println("인증 요청 번호: " + request.getPhoneNumber());

		smsService.sendSms(request.getPhoneNumber());

		return new ResponseEntity(HttpStatus.OK);
	}

	@PostMapping("/sms/verification")
	public ResponseEntity smsVerification(@RequestBody SmsDto.CertificationRequest request) {

		smsService.verifyCertification(request);

		return new ResponseEntity(HttpStatus.OK);
	}

	@PostMapping("/find/id/sms/send")
	public ResponseEntity<Void> findMyIdRequest(@RequestBody SmsDto.FindRequest request) throws CoolsmsException {

		System.out.println("인증 요청 번호: " + request.getPhoneNumber());

		userService.verifyNicknameAndPhoneNumber(request.getNickname(), request.getPhoneNumber());

		smsService.sendSms(request.getPhoneNumber());

		return new ResponseEntity(HttpStatus.OK);
	}

	@PostMapping("/find/id/sms/verification")
	public ResponseEntity<String> findMyIdVerification(@RequestBody SmsDto.FindCertificationRequest request) {

		smsService.findVerifyCertification(request);

		User findUser = userService.findByNickname(request.getNickname());

		return ResponseEntity.ok(findUser.getUserId());
	}

	@PostMapping("/find/password/sms/send")
	public ResponseEntity<Void> findMyPasswordRequest(@RequestBody SmsDto.FindPasswordRequest request) throws CoolsmsException {

		System.out.println("인증 요청 번호: " + request.getPhoneNumber());

		userService.verifyNicknameAndPhoneNumber(request.getUserId(), request.getPhoneNumber());

		smsService.sendSms(request.getPhoneNumber());

		return new ResponseEntity(HttpStatus.OK);
	}

	@PostMapping("/find/password/sms/verification")
	public ResponseEntity<String> findMyPasswordVerification(@RequestBody SmsDto.FindPasswordCertificationRequest request) {

		smsService.findPasswordVerifyCertification(request);

		User findUser = userService.findByUserId(request.getUserId());

		return ResponseEntity.ok(findUser.getPassword());
	}
}
