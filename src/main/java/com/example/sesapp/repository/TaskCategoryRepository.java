package com.example.sesapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.sesapp.entity.TaskCategory;

@Repository
public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Integer> {
  
}