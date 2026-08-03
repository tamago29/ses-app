package com.example.sesapp.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sesapp.entity.TaskCategory;
import com.example.sesapp.entity.User;
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

    public List<TaskCategory> getTaskCategoriesByUsername(String email) {
        User user = userRepository.findByEmail(email);
        return taskCategoryRepository.findByUserIdOrderById(user.getId());
    }
    
}
