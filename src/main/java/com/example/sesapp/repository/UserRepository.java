package com.example.sesapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.sesapp.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    // メールアドレスでユーザーを検索するメソッド
    User findByEmail(String email);
}