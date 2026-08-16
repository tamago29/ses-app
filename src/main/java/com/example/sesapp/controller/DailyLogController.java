package com.example.sesapp.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.sesapp.entity.DailyLog;
import com.example.sesapp.entity.TaskCategory;
import com.example.sesapp.form.DailyLogForm;
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
    public String showDailyLogList(Model model) {

        // ログインユーザーの日報一覧（List<DailyLog>）を取得
        List<DailyLog> rawLogs = dailyLogService.getDailyLogs();

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
                		.sorted(
                				Comparator.comparing(
                						TaskCategory::getNo,
                						Comparator.nullsLast(Comparator.naturalOrder())
                						)
                				)
                        .map(cat -> {
                            DailyLogForm.CategoryWork item = new DailyLogForm.CategoryWork();
                            item.setCategoryId(cat.getId());
                            item.setCategoryNo(cat.getNo());
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
            @ModelAttribute DailyLogForm form) {

        dailyLogService.saveAll(
                form.getItems(),
                form.getWorkDate()
        );

        return "redirect:/daily-log/list";
    }
}