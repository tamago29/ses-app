package com.example.sesapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
    
	// カテゴリデータをデータベースに保存する処理
    public void save(TaskCategory taskCategory) {
        // リポジトリのsaveメソッドを呼び出して、JPAの力でDBに保存する
    	taskCategoryRepository.save(taskCategory); 
    }

    public List<TaskCategory> getTaskCategoriesByUsername(String email) {
        User user = userRepository.findByEmail(email);
        return taskCategoryRepository.findByUserIdOrderById(user.getId());
    }
}
