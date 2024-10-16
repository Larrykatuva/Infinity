package com.celica.infinity.utils.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SmsRequestDto {
    @JsonProperty("sender_name")
    private String senderName;

    @JsonProperty("contact")
    private String contact;

    @JsonProperty("callback_url")
    private String callbackUrl;

    @JsonProperty("message")
    private String message;
}
