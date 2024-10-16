package com.celica.infinity.utils.notification;

import com.celica.infinity.utils.dtos.SmsRequestDto;
import com.celica.infinity.utils.dtos.SmsResponseDto;
import com.celica.infinity.utils.dtos.SmsTokenResponseDto;
import com.celica.infinity.utils.services.HttpBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    @Value("${sms.url.swift}")
    private String SMS_URL;

    @Value("${sms.url.accounts}")
    private String AUTH_URL;

    @Value("${sms.sender-name}")
    private String SENDER_NAME;

    @Value("${sms.callback}")
    private String CALLBACK;

    @Value("${sms.auth.client.id}")
    private String CLIENT_ID;

    @Value("${sms.auth.client.secret}")
    private String CLIENT_SECRET;

    private SmsTokenResponseDto getAccessToken() throws Exception {
        return new HttpBuilder<SmsTokenResponseDto>(SmsTokenResponseDto.class)
                .url(AUTH_URL)
                .method("POST")
                .formData("grant_type", "client_credentials")
                .formData("client_id", CLIENT_ID)
                .formData("client_secret", CLIENT_SECRET)
                .execute();
    }

    public void sendSms(String message, String phoneNumber) throws Exception {
        SmsTokenResponseDto token = getAccessToken();
        SmsRequestDto payload = SmsRequestDto.builder()
                .senderName(SENDER_NAME)
                .message(message)
                .contact(phoneNumber)
                .callbackUrl(CALLBACK)
                .build();
        System.out.println(payload);
        new HttpBuilder<SmsResponseDto>(SmsResponseDto.class)
                .url(SMS_URL)
                .method("POST")
                .header("Authorization", "Bearer " + token.getAccessToken())
                .body(payload)
                .execute();
    }
}
