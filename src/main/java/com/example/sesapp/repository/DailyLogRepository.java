package com.example.sesapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.sesapp.entity.DailyLog;

@Repository
public interface DailyLogRepository extends JpaRepository<DailyLog, Integer> {
    
    // 「特定のユーザーの、日報一覧だけ」を新着順（IDの降順）で全て取得するメソッド
    List<DailyLog> findByUserIdOrderByIdDesc(Integer userId);
}