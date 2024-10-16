package com.celica.infinity.common.auth.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private String fullName;
    private boolean emailVerified;
    private boolean phoneVerified;
    private String profileImage;
    private String access;
    private boolean allowed_two_factor;
    private boolean is_verified;
}
