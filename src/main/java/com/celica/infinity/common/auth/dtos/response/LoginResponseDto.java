package com.celica.infinity.common.auth.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private TokenResponseDto tokens;
    private UserResponseDto user;
}
