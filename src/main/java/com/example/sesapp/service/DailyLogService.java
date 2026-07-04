package com.example.sesapp.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.sesapp.entity.DailyLog;
import com.example.sesapp.entity.User;
import com.example.sesapp.repository.DailyLogRepository;
import com.example.sesapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class DailyLogService {

    private final DailyLogRepository dailyLogRepository;
    private final UserRepository userRepository;

    // ログインユーザーのメールアドレスから日報一覧を取得する
    public List<DailyLog> getDailyLogsByUsername(String email) {
        // SpringSecurityから届いたemailを頼りに、DBから本物のユーザーを取得
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません: " + email));
        
        // そのユーザーのIDを使って、日報一覧を「新着順」で全て取得
        return dailyLogRepository.findByUserIdOrderByIdDesc(user.getId());
    }

    // ログインユーザー情報を紐付けて日報を保存する
    public void save(DailyLog dailyLog, String email) {
        // 保存時もemailから本物のユーザーを取得
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません: " + email));
        
        dailyLog.setUser(user);
        
        // DBへ保存
        dailyLogRepository.save(dailyLog);
    }
}