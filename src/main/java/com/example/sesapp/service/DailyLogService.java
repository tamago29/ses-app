package com.example.sesapp.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.sesapp.entity.DailyLog;
import com.example.sesapp.repository.DailyLogRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DailyLogService {

    private final DailyLogRepository dailyLogRepository;

    // 特定ユーザーの日報一覧を新着順で取得する
    public List<DailyLog> getDailyLogsByUserId(Integer userId) {
        return dailyLogRepository.findByUserIdOrderByIdDesc(userId);
    }

    // 新しい日報をデータベースに保存する
    public DailyLog saveDailyLog(DailyLog dailyLog) {
        return dailyLogRepository.save(dailyLog);
    }
    
    // 日報データをデータベースに保存する処理
    public void save(DailyLog dailyLog) {
        // リポジトリのsaveメソッドを呼び出して、JPAの力でDBに保存する
        dailyLogRepository.save(dailyLog); 
    }
}