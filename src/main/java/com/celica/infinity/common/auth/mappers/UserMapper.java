package com.celica.infinity.common.auth.mappers;

import com.celica.infinity.common.auth.dtos.requests.RegisterRequestDto;
import com.celica.infinity.common.auth.dtos.response.UserResponseDto;
import com.celica.infinity.common.auth.models.User;
import com.celica.infinity.utils.Utils;
import com.celica.infinity.utils.storage.StorageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserMapper {

    private final StorageService storageService;

    public UserMapper(StorageService storageService){
        this.storageService = storageService;
    }

    public UserResponseDto toUserDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getFullName(),
                user.isEmailVerified(),
                user.isPhoneVerified(),
                storageService.getFileUrl(user.getProfileImage()),
                user.getAccess(),
                user.isAllowedTwoFactor(),
                user.isVerified()
        );
    }

    public List<UserResponseDto> toUserDto(Collection<User> users) {
        List<UserResponseDto> userDtoList = new ArrayList<>();
        users.forEach(country -> userDtoList.add(toUserDto(country)));
        return userDtoList;
    }
}
