package com.example.e_commerce.dao;

import com.example.e_commerce.dto.RequestDto.UpdateCategoryRequest;
import com.example.e_commerce.model.Category;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class CategoryDao {
    private final JdbcTemplate jdbcTemplate;
    public void createCategory(UpdateCategoryRequest category){
        jdbcTemplate.update("INSERT INTO Category (category_name, description) VALUES (?, ?)",
                category.getCategory_name(),
                category.getDescription());

    }

    public Category getCategoryById (int categoryId) {
        return  jdbcTemplate.queryForObject("SELECT * FROM Category WHERE category_id = ?",(res,rowNum)->
                new Category(
                        res.getInt("category_id"),
                        res.getString("category_name"),
                        res.getString("description")
                ),categoryId );
    }

    public List<Category> getAllCategories (){
        return jdbcTemplate.query("SELECT * FROM Category ORDER BY category_name",(res,rowNum)->
                new Category(
                        res.getInt("category_id"),
                        res.getString("category_name"),
                        res.getString("description")

                ));
    }

    public int deleteCategory (int categoryId) {
        return jdbcTemplate.update("DELETE FROM Category WHERE category_id = ?",categoryId);
    }

    public int updateCategory (int categoryId, UpdateCategoryRequest req) {
        StringBuilder sql = new StringBuilder("UPDATE Category SET ");
        List<Object> params = new ArrayList<>();

        if (req.getCategory_name() != null) {
            sql.append("category_name = ?, ");
            params.add(req.getCategory_name());
        }

        if (req.getDescription() != null) {
            sql.append("description = ?, ");
            params.add(req.getDescription());
        }

        sql.setLength(sql.length() - 2);
        sql.append(" WHERE category_id = ?");
        params.add(categoryId);

        return  jdbcTemplate.update(sql.toString(), params.toArray());
    }
}


