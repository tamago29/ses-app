package com.example.sesapp.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "daily_logs")
@Data
public class DailyLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 複数の日報が、1人のユーザーに紐付く（多対一）
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 複数の日報が、1つの業務カテゴリに紐付く
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private TaskCategory taskCategory;

    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    @Column(name = "work_hours", nullable = false)
    private Integer workHours;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}