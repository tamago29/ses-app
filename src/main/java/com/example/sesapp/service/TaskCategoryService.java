package com.example.sesapp.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.example.sesapp.entity.TaskCategory;
import com.example.sesapp.entity.User;
import com.example.sesapp.form.CategoryForm;
import com.example.sesapp.repository.TaskCategoryRepository;
import com.example.sesapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TaskCategoryService {
	
	private final TaskCategoryRepository taskCategoryRepository;
	private final UserRepository userRepository;
	
	// カテゴリ一覧を取得する
    public List<TaskCategory> findAll() {
        return taskCategoryRepository.findAll();
    }
    
    @Transactional
    public void saveAll(String email, List<TaskCategory> categories) {
        User user = userRepository.findByEmail(email);
        for (TaskCategory category : categories) {
            // User情報などをセットして一括保存
            category.setUser(user);
            taskCategoryRepository.save(category);
        }
    }

    public void validateCategories(CategoryForm categoryForm, BindingResult bindingResult, String email) {
        List<TaskCategory> categories = categoryForm.getCategories();
        if (categories == null || categories.isEmpty()) return;

        Set<Integer> seenNos = new HashSet<>();

        for (int i = 0; i < categories.size(); i++) {
            TaskCategory category = categories.get(i);
            Integer no = category.getNo();

            if (no == null) continue;

            // フォーム（リスト）内での重複チェック
            if (!seenNos.add(no)) {
                // categories[i].no フィールドに対してエラーを登録
                bindingResult.rejectValue(
                    "categories[" + i + "].no", 
                    "duplicate.no", 
                    "Noが重複しています"
                );
            }
        }
    }
    
    public List<TaskCategory> getTaskCategoriesByUsername(String email) {
        User user = userRepository.findByEmail(email);
        return taskCategoryRepository.findByUserIdOrderByNo(user.getId());
    }
    
    public void deleteById(Integer id) {
    	taskCategoryRepository.deleteById(id);
    }
    
}
