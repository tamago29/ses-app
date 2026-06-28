package com.example.sesapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.sesapp.entity.TaskCategory;
import com.example.sesapp.repository.TaskCategoryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TaskCategoryService {
	
	private final TaskCategoryRepository taskCategoryRepository;
	
	// カテゴリ一覧を取得する
    public List<TaskCategory> findAll() {
        return taskCategoryRepository.findAll();
    }
    
	// カテゴリデータをデータベースに保存する処理
    public void save(TaskCategory taskCategory) {
        // リポジトリのsaveメソッドを呼び出して、JPAの力でDBに保存する
    	taskCategoryRepository.save(taskCategory); 
    }

}
