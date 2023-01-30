package com.ewha.back.global.smsAuth.controller;

import com.ewha.back.global.smsAuth.dto.SmsDto;
import com.ewha.back.global.smsAuth.service.SmsService;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping
@RestController
@RequiredArgsConstructor
public class smsController {
    private final SmsService smsService;
    @GetMapping("/sms/send")
    public ResponseEntity<Void> smsCertification(@RequestBody SmsDto.Request request) throws CoolsmsException {

        System.out.println("인증 요청 번호: " + request.getPhoneNumber());

        smsService.sendSms(request.getPhoneNumber());

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("sms/verification")
    public ResponseEntity smsVerification(@RequestBody SmsDto.Request request) {

        smsService.verifyCertification(request);

        return new ResponseEntity(HttpStatus.OK);
    }
}
