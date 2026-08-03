package com.example.sesapp.controller;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.sesapp.entity.DailyLog;
import com.example.sesapp.entity.TaskCategory;
import com.example.sesapp.service.DailyLogService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/daily-log")
public class AnalyticsController {

    private final DailyLogService dailyLogService;

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
                // noの昇順でソート（null対策）
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