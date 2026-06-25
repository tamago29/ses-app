package com.example.sesapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.sesapp.entity.User;
import com.example.sesapp.repository.UserRepository;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // メールアドレスでユーザーを探すビジネスロジック
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}