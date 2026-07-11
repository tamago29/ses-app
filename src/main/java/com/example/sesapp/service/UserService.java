package com.example.sesapp.service;

import org.springframework.stereotype.Service;
import com.example.sesapp.entity.User;
import com.example.sesapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    // メールアドレスでユーザーを探すビジネスロジック
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}