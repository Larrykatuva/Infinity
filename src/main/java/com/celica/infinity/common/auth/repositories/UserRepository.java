package com.celica.infinity.common.auth.repositories;

import com.celica.infinity.common.auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailOrPhoneNumber(String email, String phoneNumber);

    Optional<User> findByPhoneNumberAndPhoneVerified(String phoneNumber, Boolean phoneVerified);

    Optional<User> findByEmailAndEmailVerified(String email, Boolean emailVerified);
}
