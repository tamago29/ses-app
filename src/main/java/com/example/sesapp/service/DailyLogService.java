package com.example.sesapp.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sesapp.entity.DailyLog;
import com.example.sesapp.entity.TaskCategory;
import com.example.sesapp.entity.User;
import com.example.sesapp.form.DailyLogForm;
import com.example.sesapp.repository.DailyLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DailyLogService {

    private final DailyLogRepository dailyLogRepository;
    private final LoginUserService loginUserService;

    public void saveAll(List<DailyLogForm.CategoryWork> items,
                        LocalDate workDate) {

        User user = loginUserService.getLoginUser();

        // 送信されてきたアイテムが空の場合は何もしない
        if (items == null) {
            return;
        }

        for (DailyLogForm.CategoryWork item : items) {
            
            // 時間が入力されていない、または0時間以下の行は保存スキップ
            if (item.getWorkHours() == null || item.getWorkHours().doubleValue() <= 0) {
                continue;
            }

            DailyLog log = new DailyLog();
            log.setUser(user);

            // 画面から引き継いだIDをマッピング
            TaskCategory category = new TaskCategory();
            category.setId(item.getCategoryId());
            log.setTaskCategory(category);

            log.setWorkDate(workDate);
            log.setWorkHours(item.getWorkHours());

            dailyLogRepository.save(log);
        }
    }

    public List<DailyLog> getDailyLogs() {
    	
    	User user = loginUserService.getLoginUser();
    	
    	return dailyLogRepository.findByUserIdOrderByIdDesc(user.getId());
    }
}