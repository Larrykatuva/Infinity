package com.celica.infinity.common.auth.controllers;

import com.celica.infinity.common.auth.dtos.requests.*;
import com.celica.infinity.common.auth.dtos.response.LoginResponseDto;
import com.celica.infinity.common.auth.models.User;
import com.celica.infinity.common.auth.services.AuthService;
import com.celica.infinity.utils.annotations.authentication.RequestUser;
import com.celica.infinity.utils.dtos.MessageResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@Tag(name="Auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    public ResponseEntity<MessageResponseDto> register(@RequestBody @Valid RegisterRequestDto requestDto) {
        var response = authService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("code/request")
    public ResponseEntity<MessageResponseDto> requestCode(@RequestBody @Valid RequestCodeDto codeDto) {
        var response = authService.requestCode(codeDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("code/verify")
    public ResponseEntity<MessageResponseDto> verifyCode(@RequestBody @Valid VerifyCodeRequestDto verifyDto) {
        var response = authService.verifyCode(verifyDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginDto) {
        var response = authService.login(loginDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("password/reset")
    public ResponseEntity<MessageResponseDto> resetPassword(@RequestBody @Valid SetPasswordRequestDto passwordDto) {
        var response = authService.resetPassword(passwordDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("user/update")
    public ResponseEntity<MessageResponseDto> updateUser(
            @RequestUser User user,
            @RequestBody @Valid UpdateUserRequestDto userDto
    ){
        var response = authService.updateUser(userDto, user.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
