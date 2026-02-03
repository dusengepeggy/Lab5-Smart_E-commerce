package com.example.e_commerce.service;

import com.example.e_commerce.dao.CategoryDao;
import com.example.e_commerce.dto.RequestDto.UpdateCategoryRequest;
import com.example.e_commerce.model.Category;
import com.example.e_commerce.utils.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryDao categoryDao;

    public void createCategory(UpdateCategoryRequest request) {
        categoryDao.createCategory(request);
    }

    public Category getCategoryById(int categoryId) {
        return categoryDao.getCategoryById(categoryId);
    }

    public List<Category> getAllCategories() {
        return categoryDao.getAllCategories();
    }

    @Transactional
    public Category updateCategory(int categoryId, UpdateCategoryRequest request) {
        int rows = categoryDao.updateCategory(categoryId, request);
        if (rows == 0) {
            throw new NotFoundException("Category not found");
        }
        return getCategoryById(categoryId);
    }

    public void deleteCategory(int categoryId) {
        int rows = categoryDao.deleteCategory(categoryId);
        if (rows == 0) {
            throw new NotFoundException("Category not found");
        }
    }

}
