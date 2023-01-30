package com.ewha.back.global.smsAuth.service;

import com.ewha.back.global.smsAuth.dto.SmsDto;
import com.ewha.back.global.smsAuth.repository.SmsRedisRepository;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class SmsService {

    private final SmsRedisRepository smsRedisRepository;

    public void sendSms(String phoneNumber) throws CoolsmsException {


        String api_key = "NCSQOPFYPXEWTHQV";
        String api_secret = "RCTJNNCPL8DT2AMWLTQCEKTQ6IEU43ZJ";
        Message coolsms = new Message(api_key, api_secret);

        Random rand = new Random();
        StringBuilder certificationNumber = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            certificationNumber.append(ran);
        }

        HashMap<String, String> params = new HashMap<>();

        params.put("to", phoneNumber);
        params.put("from", "01030380831");
        params.put("type", "SMS");
        params.put("text", "세대공감 아끼리: 인증번호는" + "[" + certificationNumber + "]" + "입니다.");
        params.put("app_version", "test app 1.0");

        coolsms.send(params);

        smsRedisRepository.createCertification(phoneNumber, certificationNumber.toString());
    }

    public void verifyCertification(SmsDto.Request request) {
        if (isVerified(request)) {
            throw new AuthenticationCredentialsNotFoundException("인증번호가 일치하지 않습니다.");
        } else {
            smsRedisRepository.removeCertification(request.getPhoneNumber());
        }
    }

    public Boolean isVerified(SmsDto.Request request) {
        return !(smsRedisRepository.hasKey(request.getPhoneNumber())) &&
                smsRedisRepository.getCertification(request.getPhoneNumber()).equals(request.getCertificationNumber());
    }
}
