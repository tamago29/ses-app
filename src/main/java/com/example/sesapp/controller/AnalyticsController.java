package com.example.sesapp.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.sesapp.entity.DailyLog;
import com.example.sesapp.entity.TaskCategory;
import com.example.sesapp.service.DailyLogService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/daily-log")
public class AnalyticsController {

    private final DailyLogService dailyLogService;

    // グラフ画面表示（期間絞り込み）
    @GetMapping("/analytics")
    public String showAnalytics(
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        // startDateが未指定の場合は当月1日をデフォルト値としてセット
        if (startDate == null) {
            startDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        }

        // ユーザーの全日報データを取得
        List<DailyLog> rawLogs = dailyLogService.getDailyLogs();

        // 日付フィルター処理
        final LocalDate effectiveStartDate = startDate;
        List<DailyLog> filteredLogs = rawLogs.stream()
                .filter(log -> {
                    LocalDate logDate = log.getWorkDate(); 
                    
                    if (logDate == null) {
                        return false;
                    }
                    if (effectiveStartDate != null && logDate.isBefore(effectiveStartDate)) {
                        return false;
                    }
                    if (endDate != null && logDate.isAfter(endDate)) {
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());

        // TaskCategoryごとに稼働時間を集計し、category.no の昇順にソートして LinkedHashMap に格納
        Map<TaskCategory, BigDecimal> categoryTotalMap = filteredLogs.stream()
                .collect(Collectors.groupingBy(
                        DailyLog::getTaskCategory,
                        Collectors.mapping(
                                DailyLog::getWorkHours,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey(
                        Comparator.comparing(
                                TaskCategory::getNo,
                                Comparator.nullsLast(Comparator.naturalOrder())
                        )
                ))
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
        
        // 画面のinputタグに値を保持させるためModelに追加
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "daily-log/analytics"; 
    }
}