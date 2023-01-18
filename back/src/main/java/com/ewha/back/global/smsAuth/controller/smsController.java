package com.ewha.back.global.smsAuth.controller;

import com.ewha.back.global.smsAuth.service.smsService;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.web.bind.annotation.*;

@RequestMapping
@RestController
@RequiredArgsConstructor
public class smsController {
    private final smsService smsService;
    @GetMapping("/users/signup/sms")
    public @ResponseBody String smsCertification(@RequestParam(value = "to") String to) throws CoolsmsException {

        System.out.println("인증 요청 번호: " + to);

        return smsService.certifiedPhoneNumber(to);
    }
}
