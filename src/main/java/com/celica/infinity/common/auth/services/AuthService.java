package com.celica.infinity.common.auth.services;

import com.celica.infinity.common.auth.dtos.requests.*;
import com.celica.infinity.common.auth.dtos.response.LoginResponseDto;
import com.celica.infinity.common.auth.dtos.response.TokenResponseDto;
import com.celica.infinity.common.auth.enums.Context;
import com.celica.infinity.common.auth.models.Code;
import com.celica.infinity.common.auth.models.User;
import com.celica.infinity.utils.Utils;
import com.celica.infinity.utils.dtos.MessageResponseDto;
import com.celica.infinity.utils.exceptions.BadRequestException;
import com.celica.infinity.utils.storage.StorageService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class AuthService {

    private final UserService userService;
    private final CodeService codeService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;

    public AuthService(
            UserService userService,
            CodeService codeService,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            StorageService storageService
    ){
        this.userService = userService;
        this.codeService = codeService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.storageService = storageService;
    }

    public MessageResponseDto requestCode(RequestCodeDto codeDto) {
        User user = userService.getUserByUsername(codeDto.getUsername()).orElseThrow(
                () -> new BadRequestException("User not found", "User with given email or phone number does not exists")
        );
        codeService.sendCode(user, codeDto.getContext());
        return new MessageResponseDto("Your "+codeDto.getContext().toString().toLowerCase() +
                " code has been send to your email");
    }

    public MessageResponseDto verifyCode(VerifyCodeRequestDto verifyDto) {
        User user = userService.getUserByUsername(verifyDto.getUsername()).orElseThrow(
                () -> new BadRequestException("User not found", "User with given email or phone number does not exists")
        );
        Code code = codeService.getCode(user, verifyDto.getCode(), verifyDto.getContext()).orElseThrow(
                () -> new BadRequestException("Code not found", "Invalid user code")
        );
        if (code.getExpiry().isAfter(LocalDateTime.now()))
            throw new BadRequestException("Code expired", "Code already expired consider generating another");
        switch (code.getContext()){
            case VERIFICATION:
                user.setVerified(true);
                if (user.getUsername().equals(user.getEmail()))
                    user.setEmailVerified(true);
                if (user.getUsername().equals(user.getPhoneNumber()))
                    user.setPhoneVerified(true);
            case VERIFY_EMAIL:
                user.setEmailVerified(true);
            case VERIFY_PHONE:
                user.setPhoneVerified(true);
        }
        userService.saveUser(user);
        return new MessageResponseDto("Code verified successfully");
    }

    public MessageResponseDto register(RegisterRequestDto registerRequestDto) {
        if (!registerRequestDto.getPassword().equals(registerRequestDto.getConfirmPassword()))
            throw new BadRequestException("Password do not match", "Password and confirm password do not match");
        String email = registerRequestDto.getEmail();
        String phoneNumber = registerRequestDto.getPhoneNumber();
        String username = null;
        if (!Utils.isStringNullOrEmpty(email)){
            username = email;
        } else {
            username = phoneNumber;
        }
        Optional<User> existingUser = userService.getUserByUsername(username);
        if (existingUser.isPresent())
            throw new BadRequestException("User exists", "User with given email or phone number already exists");
        Optional<User> existingPhone = userService.getUserByPhoneNumber(phoneNumber);
        if (existingPhone.isPresent())
            throw new BadRequestException("Phone exists", "User with the given phone number already exists");
        Optional<User> existingEmail = userService.getUserByEmail(email);
        if (existingEmail.isPresent())
            throw new BadRequestException("Email exists", "User with given email already exists");
        User user = User.builder()
                .username(Utils.cleanString(username))
                .email(Utils.cleanString(email))
                .phoneNumber(Utils.cleanString(phoneNumber))
                .fullName(Utils.cleanString(registerRequestDto.getFullName()))
                .password(passwordEncoder.encode(Utils.cleanString(registerRequestDto.getPassword())))
                .build();
        userService.saveUser(user);
        codeService.sendCode(user, Context.VERIFICATION);
        return new MessageResponseDto("User registration successful check verification email");
    }

    public LoginResponseDto login(LoginRequestDto loginDto) {
        User user = userService.getUserByUsername(loginDto.getUsername()).orElseThrow(
                () -> new BadRequestException("User not found", "User with given email or phone number does not exists")
        );
        if (!user.isVerified())
            throw new BadRequestException("User not verified", "User not verified");
        if (user.getPhoneNumber().equals(loginDto.getUsername()) && !user.isPhoneVerified())
            throw new BadRequestException("Phone not verified", "User phone number not verified");
        if (user.getEmail().equals(loginDto.getUsername()) && !user.isEmailVerified())
            throw new BadRequestException("Email not verified", "User email not verified");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );
        String accessToken = jwtService.generateToken(user);
        return new LoginResponseDto(
               new TokenResponseDto(accessToken), userService.userToDto(user)
        );
    }

    public MessageResponseDto resetPassword(SetPasswordRequestDto passwordDto) {
        if (!passwordDto.getPassword().equals(passwordDto.getConfirmPassword()))
            throw new BadRequestException("Password do not match", "Password and confirm password do not match");
        User user = userService.getUserByUsername(passwordDto.getUsername()).orElseThrow(
                () -> new BadRequestException("User not found", "User with given email or phone number does not exists")
        );
        if (!user.isVerified())
            throw new BadRequestException("User not verified", "User not verified");
        verifyCode(new VerifyCodeRequestDto(
                passwordDto.getCode(),
                passwordDto.getUsername(),
                Context.RESET
        ));
        user.setPassword(passwordEncoder.encode(Utils.cleanString(passwordDto.getPassword())));
        userService.saveUser(user);
        return new MessageResponseDto("Password reset successfully");
    }

    public MessageResponseDto updateUser(UpdateUserRequestDto userDto, String username) {
        User user = userService.getUserByUsername(username).orElseThrow(
                () -> new BadRequestException("User not found", "User with given email or phone number does not exists")
        );
        boolean update = false;
        String fileName = null;
        if (!Utils.isStringNullOrEmpty(userDto.getFullName())){
            user.setFullName(userDto.getFullName());
            update = true;
        }
        if (!userDto.getProfileImage().isEmpty()){
            fileName = storageService.getFilename(userDto.getProfileImage());
            user.setProfileImage(fileName);
            update = true;
        }
        if (!userDto.getEmail().isEmpty()){
            user.setEmail(userDto.getEmail());
            user.setEmailVerified(false);
            update = true;
        }
        if (!userDto.getPhoneNumber().isEmpty()){
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setPhoneVerified(false);
            update = true;
        }
        if (!update)
            throw new BadRequestException("Nothing to update", "No new changes requested");
        userService.saveUser(user);
        String message = "User profile updated successfully";
        if (!userDto.getProfileImage().isEmpty())
            storageService.saveFile(userDto.getProfileImage(), fileName);
        if (!userDto.getPhoneNumber().isEmpty()){
            Code code = codeService.generateCode(user, Context.VERIFY_PHONE);
            codeService.sendCodeSms(user, Context.VERIFY_PHONE, code);
            message = "Otp code to verify phone number send to "+userDto.getPhoneNumber();
        }
        if (!userDto.getEmail().isEmpty()){
            Code code = codeService.generateCode(user, Context.VERIFY_EMAIL);
            codeService.sendCodeEmail(user, Context.VERIFY_EMAIL, code);
            message = "Otp code to verify email send to "+userDto.getEmail();
        }
        return new MessageResponseDto(message);
    }

}
