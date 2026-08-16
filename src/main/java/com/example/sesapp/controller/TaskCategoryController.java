package com.example.sesapp.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String showCategoryList(Model model) {
      
        // DBからログインユーザーのカテゴリ一覧を取得
        List<TaskCategory> rawCategories = taskCategoryService.findAll();
        
        List<TaskCategory> categories = rawCategories.stream()
        	    .sorted(
        	        Comparator.comparing(
        	            TaskCategory::getNo,
        	            Comparator.nullsLast(Comparator.naturalOrder())
        	        )
        	    )
        	    .toList();

        // CategoryForm オブジェクトを作成し、取得したリストをセット
        CategoryForm categoryForm = new CategoryForm();
        categoryForm.setCategories(categories);
        
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
            @ModelAttribute CategoryForm categoryForm,
            BindingResult bindingResult,
            Model model) {
        
        // バリデーション実行
        taskCategoryService.validateCategories(categoryForm, bindingResult);
        
        // エラーがある場合は保存せずに元の画面を再表示
        if (bindingResult.hasErrors()) {
            // エラー発生時は再表示（ModelにcategoryFormが入った状態で元のHTMLへ）
            return "daily-log/category-list";
        }
        
        taskCategoryService.saveAll(categoryForm.getCategories());
        
        return "redirect:/daily-log/category-list";
    }
    
    //削除ボタンが押されたとき
    @PostMapping(value = "category-list", params = "delete_id")
    public String deleteRow(@RequestParam("delete_id") Integer id) {
    	taskCategoryService.deleteById(id);
    	return "redirect:/daily-log/category-list";
    }
}