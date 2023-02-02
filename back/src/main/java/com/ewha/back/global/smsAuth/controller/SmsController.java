package com.ewha.back.global.smsAuth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.nurigo.java_sdk.exceptions.CoolsmsException;

import com.ewha.back.global.smsAuth.dto.SmsDto;
import com.ewha.back.global.smsAuth.service.SmsService;

import lombok.RequiredArgsConstructor;

@RequestMapping
@RestController
@RequiredArgsConstructor
public class SmsController {
	private final SmsService smsService;

	@GetMapping("/sms/send")
	public ResponseEntity<Void> smsCertification(@RequestBody SmsDto.Request request) throws CoolsmsException {

		System.out.println("인증 요청 번호: " + request.getPhoneNumber());

		smsService.sendSms(request.getUserId(), request.getPhoneNumber());

		return new ResponseEntity(HttpStatus.OK);
	}

	@PostMapping("sms/verification")
	public ResponseEntity smsVerification(@RequestBody SmsDto.Request request) {

		smsService.verifyCertification(request);

		return new ResponseEntity(HttpStatus.OK);
	}
}
