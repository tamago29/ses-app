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

import com.example.sesapp.entity.TaskCategory;
import com.example.sesapp.form.CategoryForm;
import com.example.sesapp.service.TaskCategoryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/daily-log")
public class TaskCategoryController {

    private final TaskCategoryService taskCategoryService;

    @GetMapping("/category-list")
    public String showCategoryList(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {
        
        String email = userDetails.getUsername();
        
        // DBからログインユーザーのカテゴリ一覧を取得
        List<TaskCategory> rawCategories = taskCategoryService.getTaskCategoriesByUsername(email);
        
        // CategoryForm オブジェクトを作成し、取得したリストをセット
        CategoryForm categoryForm = new CategoryForm();
        categoryForm.setCategories(rawCategories);
        
        // "categoryForm"をModel にセット！
        model.addAttribute("categoryForm", categoryForm);
        
        return "daily-log/category-list";
    }
    
    // 追加ボタンが押されたとき（新規の空行を追加）
    @PostMapping(value = "/category-list", params = "add")
    public String addRow(@ModelAttribute CategoryForm categoryForm, Model model) {
        
        // Form内のリストがnullの場合の安全策
        if (categoryForm.getCategories() == null) {
            categoryForm.setCategories(new java.util.ArrayList<>());
        }

        TaskCategory newCategory = new TaskCategory();
        // 現在の件数+1をNoの初期値にする
        newCategory.setNo(categoryForm.getCategories().size() + 1);
        
        categoryForm.getCategories().add(newCategory);
        
        // 更新した Form を Model にセットして画面に渡す
        model.addAttribute("categoryForm", categoryForm);
        
        return "daily-log/category-list";
    }

    // 保存ボタンが押されたとき（一括保存）
    @PostMapping(value = "/category-list", params = "save")
    public String saveCategories(
            @AuthenticationPrincipal UserDetails userDetails,
            @ModelAttribute CategoryForm categoryForm) {
        
        String email = userDetails.getUsername();
        taskCategoryService.saveAll(email, categoryForm.getCategories());
        
        return "redirect:/daily-log/category-list";
    }
}