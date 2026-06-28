package com.example.sesapp.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.sesapp.entity.DailyLog;
import com.example.sesapp.entity.TaskCategory;
import com.example.sesapp.entity.User;
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
    public String showDailyLogList(Model model) {
    	
    	System.out.println("DailyLogControllerが呼ばれました");
        
        // 開発中のテストとして、ユーザーID「1」のデータを取得
        Integer mockUserId = 1; 
        List<DailyLog> logs = dailyLogService.getDailyLogsByUserId(mockUserId);
        
        model.addAttribute("logs", logs);
        
        return "daily-log/list"; 
    }
    
    // 新規登録画面を表示
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        
        // 空っぽの日報オブジェクトをModelに詰める
        DailyLog emptyLog = new DailyLog();
        model.addAttribute("dailyLog", emptyLog);
        
        // DBからすべてのカテゴリ一覧を取得して、Modelに詰める
        // ※ もし taskCategoryService がなければ、リポジトリから直接 findAll() してもOKです
        List<TaskCategory> categories = taskCategoryService.findAll();
        model.addAttribute("categories", categories);
        
        return "daily-log/register";
    }
    
    // 画面から送られてきたデータをDBに保存
    @PostMapping("/register")
    public String registerDailyLog(@ModelAttribute DailyLog dailyLog) {
        
        // 👤 ① 仮のユーザーオブジェクト（ID: 1）を作成する
        // (※ 本来はDBから本物のユーザーを1件取得するのが理想ですが、まずは動かすために枠だけ作ります)
        User mockUser = new User();
        mockUser.setId(1);
        
        // 🔗 ② 日報オブジェクトに、この仮のユーザーをセットして「穴埋め」する
        dailyLog.setUser(mockUser);
        
        // サービスを使って、中身が揃った日報をDBに保存
        dailyLogService.save(dailyLog); 
        
        // 画面のセレクトボックスで選ばれたカテゴリIDは、JPAの力で自動的に dailyLog の中に紐付いて届く
        
        return "redirect:/daily-log/list";
    }
}