package com.celica.infinity.common.auth.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateUserRequestDto {
    @Size(max = 255, message = "Fullname should not exceed 255 characters")
    private String fullName;
    private MultipartFile profileImage;
    @Email
    @Size(max = 255, message = "Email should not exceed 255 characters")
    private String email;
    @Size(min = 9, max = 12, message = "Phone number should be between 9 and 12 characters")
    private String phoneNumber;
}
