package com.example.sesapp.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.sesapp.entity.DailyLog;
import com.example.sesapp.service.DailyLogService;

@Controller
@RequestMapping("/daily-log") 
public class DailyLogController {

    @Autowired
    private DailyLogService dailyLogService;

    // 日報一覧画面を表示する受付窓口
    @GetMapping("/list") 
    public String showDailyLogList(Model model) {
    	
    	System.out.println("DailyLogControllerが呼ばれました");
        
        // 開発中のテストとして、ユーザーID「1」のデータを取得
        Integer mockUserId = 1; 
        List<DailyLog> logs = dailyLogService.getDailyLogsByUserId(mockUserId);
        
        model.addAttribute("logs", logs);
        
        return "daily-log/list"; 
    }
}