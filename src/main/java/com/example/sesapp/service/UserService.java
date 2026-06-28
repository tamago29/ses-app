package com.example.sesapp.service;

import org.springframework.stereotype.Service;
import com.example.sesapp.entity.User;
import com.example.sesapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    // メールアドレスでユーザーを探すビジネスロジック
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}