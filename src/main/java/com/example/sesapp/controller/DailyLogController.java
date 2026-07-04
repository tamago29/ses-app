package com.example.sesapp.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.sesapp.entity.DailyLog;
import com.example.sesapp.entity.TaskCategory;
import com.example.sesapp.service.DailyLogService;
import com.example.sesapp.service.TaskCategoryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/daily-log") 
public class DailyLogController {

    private final DailyLogService dailyLogService;
    private final TaskCategoryService taskCategoryService;

    // 日報一覧画面を表示
    @GetMapping("/list") 
   
    public String showDailyLogList(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        
        // ログインID（email）を取得
        String username = userDetails.getUsername(); 
        
        // ログインIDを使って日報一覧を取得
        List<DailyLog> logs = dailyLogService.getDailyLogsByUsername(username);
        
        model.addAttribute("logs", logs);
        return "daily-log/list"; 
    }
    
    // 新規登録画面を表示
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        
        // 空の日報オブジェクトをModelに詰める
        DailyLog emptyLog = new DailyLog();
        model.addAttribute("dailyLog", emptyLog);
        
        // DBからすべてのカテゴリ一覧を取得して、Modelに詰める
        List<TaskCategory> categories = taskCategoryService.findAll();
        model.addAttribute("categories", categories);
        
        return "daily-log/register";
    }
    
    // 画面から送られてきたデータをDBに保存
    @PostMapping("/register")
    // 保存時も UserDetails を受け取る
    public String registerDailyLog(@ModelAttribute DailyLog dailyLog, @AuthenticationPrincipal UserDetails userDetails) {
        
        String username = userDetails.getUsername();
        
        // データとログインIDを渡して保存
        dailyLogService.save(dailyLog, username); 
        
        return "redirect:/daily-log/list";
    }
}