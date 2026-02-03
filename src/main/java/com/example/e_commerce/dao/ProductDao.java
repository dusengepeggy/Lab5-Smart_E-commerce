package com.example.e_commerce.dao;

import com.example.e_commerce.model.Product;
import com.example.e_commerce.dto.RequestDto.ProductQueryParams;
import com.example.e_commerce.dto.ResponseDto.PagedResponse;
import com.example.e_commerce.dto.ResponseDto.ProductWithCategory;
import com.example.e_commerce.dto.ResponseDto.ProductWithCategoryAndStock;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Product> PRODUCT_ROW_MAPPER = (rs, rowNum) ->
            new Product(
                    rs.getInt("product_id"),
                    rs.getInt("category_id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getBigDecimal("price"),
                    rs.getTimestamp("created_at").toLocalDateTime()
            );

    private static final RowMapper<ProductWithCategory> PRODUCT_WITH_CATEGORY_ROW_MAPPER = (rs, rowNum) ->
            new ProductWithCategory(
                    rs.getInt("product_id"),
                    rs.getInt("category_id"),
                    rs.getString("category_name"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getBigDecimal("price"),
                    rs.getTimestamp("created_at").toLocalDateTime()
            );

    private static final RowMapper<ProductWithCategoryAndStock> PRODUCT_WITH_CATEGORY_AND_STOCK_ROW_MAPPER = (rs, rowNum) ->
            new ProductWithCategoryAndStock(
                    rs.getInt("product_id"),
                    rs.getInt("category_id"),
                    rs.getString("category_name"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getBigDecimal("price"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getInt("stock_quantity")
            );


    public int createProduct(Product product) {
        return jdbcTemplate.update(
                """
                INSERT INTO Product (category_id, name, description, price)
                VALUES (?, ?, ?, ?)
                """,
                product.getCategory_id(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }

    public Optional<Product> getProductById(int productId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "SELECT * FROM Product WHERE product_id = ?",
                            PRODUCT_ROW_MAPPER,
                            productId
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Product> getAllProducts() {
        return jdbcTemplate.query(
                "SELECT * FROM Product ORDER BY name",
                PRODUCT_ROW_MAPPER
        );
    }

    public List<Product> getProductsByCategory(int categoryId) {
        return jdbcTemplate.query(
                "SELECT * FROM Product WHERE category_id = ? ORDER BY name",
                PRODUCT_ROW_MAPPER,
                categoryId
        );
    }

    public List<Product> searchProductsByName(String searchTerm) {
        return jdbcTemplate.query(
                "SELECT * FROM Product WHERE LOWER(name) LIKE LOWER(?) ORDER BY name",
                PRODUCT_ROW_MAPPER,
                "%" + searchTerm + "%"
        );
    }

    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return jdbcTemplate.query(
                "SELECT * FROM Product WHERE price BETWEEN ? AND ? ORDER BY price",
                PRODUCT_ROW_MAPPER,
                minPrice,
                maxPrice
        );
    }


    public int updateProduct(int productId, Product product) {
        StringBuilder sql = new StringBuilder("UPDATE Product SET ");
        List<Object> params = new ArrayList<>();

        if (product.getCategory_id() != 0) {
            sql.append("category_id = ?, ");
            params.add(product.getCategory_id());
        }

        if (product.getName() != null) {
            sql.append("name = ?, ");
            params.add(product.getName());
        }

        if (product.getDescription() != null) {
            sql.append("description = ?, ");
            params.add(product.getDescription());
        }

        if (product.getPrice() != null) {
            sql.append("price = ?, ");
            params.add(product.getPrice());
        }

        if (params.isEmpty()) {
            return 0;
        }

        sql.setLength(sql.length() - 2); // remove trailing comma
        sql.append(" WHERE product_id = ?");
        params.add(productId);

        return jdbcTemplate.update(sql.toString(), params.toArray());
    }



    public int deleteProduct(int productId) {
        return jdbcTemplate.update(
                "DELETE FROM Product WHERE product_id = ?",
                productId
        );
    }


    public PagedResponse<ProductWithCategory> getAllProductsWithCategory(
            ProductQueryParams query,
            Integer limit,
            Integer offset
    ) {
        StringBuilder base = new StringBuilder(
                """
                SELECT p.product_id,
                       p.category_id,
                       c.category_name,
                       p.name,
                       p.description,
                       p.price,
                       p.created_at
                FROM Product p
                JOIN Category c ON p.category_id = c.category_id
                WHERE 1=1
                """
        );
        List<Object> sqlParams = new ArrayList<>();

        if (query.getCategoryId() != null) {
            base.append(" AND p.category_id = ?");
            sqlParams.add(query.getCategoryId());
        }
        if (query.getQ() != null && !query.getQ().isBlank()) {
            base.append(" AND LOWER(p.name) LIKE LOWER(?)");
            sqlParams.add("%" + query.getQ().trim() + "%");
        }
        if (query.getMinPrice() != null) {
            base.append(" AND p.price >= ?");
            sqlParams.add(query.getMinPrice());
        }
        if (query.getMaxPrice() != null) {
            base.append(" AND p.price <= ?");
            sqlParams.add(query.getMaxPrice());
        }

        String orderBy = switch (query.getSortBy() == null ? "" : query.getSortBy().trim().toLowerCase()) {
            case "price" -> "p.price";
            case "created_at", "createdat" -> "p.created_at";
            case "category_name", "categoryname" -> "c.category_name";
            case "name", "" -> "p.name";
            default -> "p.name";
        };
        boolean desc = query.getSortDir() != null && query.getSortDir().trim().equalsIgnoreCase("desc");
        base.append(" ORDER BY ").append(orderBy).append(desc ? " DESC" : " ASC");

        StringBuilder countSql = new StringBuilder(
                "SELECT COUNT(*) FROM Product p JOIN Category c ON p.category_id = c.category_id WHERE 1=1"
        );
        if (query.getCategoryId() != null) countSql.append(" AND p.category_id = ?");
        if (query.getQ() != null && !query.getQ().isBlank()) countSql.append(" AND LOWER(p.name) LIKE LOWER(?)");
        if (query.getMinPrice() != null) countSql.append(" AND p.price >= ?");
        if (query.getMaxPrice() != null) countSql.append(" AND p.price <= ?");

        long total;
        List<ProductWithCategory> items;
        if (limit != null && offset != null) {
            Long totalVal = jdbcTemplate.queryForObject(countSql.toString(), Long.class, sqlParams.toArray());
            total = totalVal == null ? 0L : totalVal;
            if (total == 0L) {
                items = List.of();
            } else {
                base.append(" LIMIT ? OFFSET ?");
                sqlParams.add(limit);
                sqlParams.add(offset);
                items = jdbcTemplate.query(base.toString(), PRODUCT_WITH_CATEGORY_ROW_MAPPER, sqlParams.toArray());
            }
        } else {
            items = jdbcTemplate.query(base.toString(), PRODUCT_WITH_CATEGORY_ROW_MAPPER, sqlParams.toArray());
            total = items.size();
        }

        int size = limit != null ? limit : items.size();
        int page = (limit != null && limit > 0) ? offset / limit : 0;
        int totalPages = size == 0 ? 0 : (int) Math.ceil(total / (double) size);
        return new PagedResponse<>(items, page, size, total, totalPages);
    }

    public List<ProductWithCategory> getProductsByCategoryWithCategory(int categoryId) {
        return jdbcTemplate.query(
                """
                SELECT p.product_id,
                       p.category_id,
                       c.category_name,
                       p.name,
                       p.description,
                       p.price,
                       p.created_at
                FROM Product p
                JOIN Category c ON p.category_id = c.category_id
                WHERE p.category_id = ?
                ORDER BY p.name
                """,
                PRODUCT_WITH_CATEGORY_ROW_MAPPER,
                categoryId
        );
    }

    public Optional<ProductWithCategoryAndStock> getProductByIdWithCategoryAndStock(int productId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            """
                            SELECT p.product_id,
                                   p.category_id,
                                   c.category_name,
                                   p.name,
                                   p.description,
                                   p.price,
                                   p.created_at,
                                   COALESCE(i.stock_quantity, 0) AS stock_quantity
                            FROM Product p
                            JOIN Category c ON p.category_id = c.category_id
                            LEFT JOIN Inventory i ON i.product_id = p.product_id
                            WHERE p.product_id = ?
                            """,
                            PRODUCT_WITH_CATEGORY_AND_STOCK_ROW_MAPPER,
                            productId
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
