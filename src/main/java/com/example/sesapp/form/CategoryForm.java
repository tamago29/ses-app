package com.example.sesapp.form;

import java.util.ArrayList;
import java.util.List;

import com.example.sesapp.entity.TaskCategory;

import lombok.Data;

@Data
public class CategoryForm {
    private List<TaskCategory> categories = new ArrayList<>();
}
