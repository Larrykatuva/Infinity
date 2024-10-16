package com.celica.infinity.utils.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SmsResponseDto {
    @JsonProperty("message_id")
    private String messageId;

    @JsonProperty("message")
    private String[] message;

    @JsonProperty("telco")
    private String telco;
}
