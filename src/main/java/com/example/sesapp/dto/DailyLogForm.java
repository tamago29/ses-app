package com.example.sesapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class DailyLogForm {

    private LocalDate workDate;

    private List<CategoryWork> items;

    @Data
    public static class CategoryWork {
        private Integer categoryId;
        private String categoryName;
        private BigDecimal workHours;
    }
}