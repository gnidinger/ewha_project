package com.ewha.back.global.smsAuth.service;

import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class smsService {

    public String certifiedPhoneNumber(String phoneNumber) throws CoolsmsException {


        String api_key = "NCSQOPFYPXEWTHQV";
        String api_secret = "RCTJNNCPL8DT2AMWLTQCEKTQ6IEU43ZJ";
        Message coolsms = new Message(api_key, api_secret);

        Random rand = new Random();
        StringBuilder numStr = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr.append(ran);
        }

        HashMap<String, String> params = new HashMap<>();

        params.put("to", phoneNumber);
        params.put("from", "01030380831");
        params.put("type", "SMS");
        params.put("text", "세대공감 아끼리: 인증번호는" + "[" + numStr + "]" + "입니다.");
        params.put("app_version", "test app 1.0");

        coolsms.send(params);

        return numStr.toString();
    }
}
