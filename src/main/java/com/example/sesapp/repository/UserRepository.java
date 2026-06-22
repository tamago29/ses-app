package com.example.sesapp.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.sesapp.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    // メールアドレスでユーザーを検索するメソッド
    Optional<User> findByEmail(String email);
}