package com.example.sesapp.controller;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.sesapp.dto.DailyLogForm;
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

    // 一覧表示
    @GetMapping("/list")
    public String showDailyLogList(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        String email = userDetails.getUsername();

        // ログインユーザーの日報一覧（List<DailyLog>）を取得
        List<DailyLog> rawLogs = dailyLogService.getDailyLogsByUsername(email);

        // 日付（workDate）ごとにデータをグループ化し、グループ内をnoの昇順にソート
        Map<java.time.LocalDate, List<DailyLog>> groupedLogs = rawLogs.stream()
                .collect(Collectors.groupingBy(
                        DailyLog::getWorkDate,
                        () -> new TreeMap<>(Collections.reverseOrder()), // 「日付が新しい順」にソート
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    // グループ内のリストをtaskCategoryのno昇順にソート
                                    list.sort(Comparator.comparing(
                                            log -> log.getTaskCategory().getNo(),
                                            Comparator.nullsLast(Comparator.naturalOrder()) // null安全対策
                                            ));
                                    return list;
                                }
                       )
        ));

        // グループ化したデータをモデルに詰める
        model.addAttribute("groupedLogs", groupedLogs);
        return "daily-log/list";
    }

    // 日報登録画面表示
    @GetMapping("/register")
    public String showRegisterForm(Model model) {

    	//作業カテゴリ一覧取得
        List<TaskCategory> categories = taskCategoryService.findAll();

        DailyLogForm form = new DailyLogForm();

        form.setItems(
                categories.stream()
                        .map(cat -> {
                            DailyLogForm.CategoryWork item = new DailyLogForm.CategoryWork();
                            item.setCategoryId(cat.getId());
                            item.setCategoryName(cat.getName());
                            return item;
                        })
                        .toList()
        );

        model.addAttribute("form", form);

        return "daily-log/register";
    }

    // 登録処理
    @PostMapping("/register")
    public String register(
            @ModelAttribute DailyLogForm form,
            @AuthenticationPrincipal UserDetails userDetails) {

        dailyLogService.saveAll(
                form.getItems(),
                userDetails.getUsername(),
                form.getWorkDate()
        );

        return "redirect:/daily-log/list";
    }
    
    @GetMapping("/category-list")
    public String showCategoryList(
    		@AuthenticationPrincipal UserDetails userDetails,
    		Model model) {
    	
    	String email = userDetails.getUsername();
    	
    	// ログインユーザーのカテゴリ一覧（List<TaskCategory>）を取得
        List<TaskCategory> rawCategories = taskCategoryService.getTaskCategoriesByUsername(email);
        
        model.addAttribute("rawCategories", rawCategories);
        return "daily-log/category-list";
    }
    
    // グラフ画面表示
    @GetMapping("/analytics")
    public String showAnalytics(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        String email = userDetails.getUsername();

        // ユーザーの全日報データを取得
        List<DailyLog> rawLogs = dailyLogService.getDailyLogsByUsername(email);

        // TaskCategoryごとに稼働時間を集計し、category.no の昇順にソートして LinkedHashMap に格納
        Map<TaskCategory, BigDecimal> categoryTotalMap = rawLogs.stream()
                .collect(Collectors.groupingBy(
                        DailyLog::getTaskCategory,                                    // キー：TaskCategoryオブジェクト
                        Collectors.mapping(
                                DailyLog::getWorkHours,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)) // 値：時間の合計
                ))
                .entrySet().stream()
                // noの昇順でソート（null安全対策付き）
                .sorted(Map.Entry.comparingByKey(
                        Comparator.comparing(
                                TaskCategory::getNo,
                                Comparator.nullsLast(Comparator.naturalOrder())
                        )
                ))
                // ソート結果の順番を保持したまま LinkedHashMap へ詰める
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldVal, newVal) -> oldVal,
                        LinkedHashMap::new
                ));

        // カテゴリ名（String）のリストと合計時間のリストを抽出し、モデルに詰める
        List<String> chartLabels = categoryTotalMap.keySet().stream()
                .map(TaskCategory::getName)
                .collect(Collectors.toList());

        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("chartData", categoryTotalMap.values());

        return "daily-log/analytics"; 
    }
}