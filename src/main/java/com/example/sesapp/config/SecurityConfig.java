package com.example.sesapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.sesapp.entity.User;
import com.example.sesapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository; 

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated() // すべての画面でログインを必須にする
            )
            .formLogin(form -> form
                .defaultSuccessUrl("/daily-log/list", true) // ログイン成功時に日報一覧画面へ飛ばす
                .permitAll()
            )
            .logout(logout -> logout
                .permitAll()
            );
        
        return http.build();
    }

    // SpringSecurityにDBの「email」を見に行くように教える設定
    @Bean
    protected UserDetailsService userDetailsService() {
        return username -> {
            // 画面のユーザー名欄に入力されたメールアドレスでDBを検索
            User user = userRepository.findByEmail(username);

            // Spring Securityが理解できる形式に変換して返す
            return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                    .password(user.getPassword()) // 暗号化されたパスワード
                    .roles("USER")
                    .build();
        };
    }

    //パスワードの暗号化方式（BCrypt）を指定する設定
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /* 暗号化せず、生テキストのまま比較する設定）
    @Bean
    protected PasswordEncoder passwordEncoder() {
        return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
    }*/
}