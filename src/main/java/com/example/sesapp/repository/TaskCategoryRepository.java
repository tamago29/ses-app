package com.example.sesapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sesapp.entity.TaskCategory;

@Repository
public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Integer> {
	
	// 「特定のユーザーのカテゴリ一覧のみ」をID順で全て取得するメソッド
    List<TaskCategory> findByUserIdOrderById(Integer userId);
}