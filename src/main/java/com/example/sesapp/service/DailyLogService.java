package com.example.sesapp.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.sesapp.entity.DailyLog;
import com.example.sesapp.repository.DailyLogRepository;

@Service
public class DailyLogService {

    @Autowired
    private DailyLogRepository dailyLogRepository;

    // 特定ユーザーの日報一覧を新着順で取得する
    public List<DailyLog> getDailyLogsByUserId(Integer userId) {
        return dailyLogRepository.findByUserIdOrderByIdDesc(userId);
    }

    // 新しい日報をデータベースに保存する
    public DailyLog saveDailyLog(DailyLog dailyLog) {
        return dailyLogRepository.save(dailyLog);
    }
}