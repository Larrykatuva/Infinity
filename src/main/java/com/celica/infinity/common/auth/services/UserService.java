package com.celica.infinity.common.auth.services;

import com.celica.infinity.common.auth.dtos.response.UserResponseDto;
import com.celica.infinity.common.auth.models.User;
import com.celica.infinity.common.auth.repositories.UserRepository;
import com.celica.infinity.utils.storage.AwsStorageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AwsStorageService awsService;

    public UserService(UserRepository userRepository, AwsStorageService awsService) {
        this.userRepository = userRepository;
        this.awsService = awsService;
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByEmailOrPhoneNumber(username, username);
    }

    public Optional<User> getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumberAndPhoneVerified(phoneNumber, true);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmailAndEmailVerified(email, true);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public UserResponseDto userToDto(User user) {
        return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getPhoneNumber(),
                user.getFullName(), user.isEmailVerified(), user.isPhoneVerified(),
                awsService.getPresignedUrl(user.getProfileImage()), user.getAccess(), user.isAllowedTwoFactor(),
                user.isVerified());
    }

    public List<UserResponseDto> userToDto(Collection<User> users) {
        List<UserResponseDto> userDtoList = new ArrayList<>();
        users.forEach(country -> userDtoList.add(userToDto(country)));
        return userDtoList;
    }

}
