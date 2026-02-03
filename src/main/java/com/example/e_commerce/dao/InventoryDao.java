package com.example.e_commerce.dao;

import com.example.e_commerce.model.Inventory;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class InventoryDao {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Inventory> INVENTORY_ROW_MAPPER = (rs, rowNum) ->
            new Inventory(
                    rs.getInt("inventory_id"),
                    rs.getInt("product_id"),
                    rs.getInt("stock_quantity"),
                    rs.getString("warehouse_location"),
                    rs.getTimestamp("updated_at")
            );

    public int createInventory(Inventory inventory) {
        return jdbcTemplate.update(
                """
                INSERT INTO Inventory (product_id, stock_quantity, warehouse_location)
                VALUES (?, ?, ?)
                """,
                inventory.getProduct_id(),
                inventory.getStock_quantity(),
                inventory.getWarehouse_location()
        );
    }

    public Optional<Inventory> getInventoryById(int inventoryId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "SELECT * FROM Inventory WHERE inventory_id = ?",
                            INVENTORY_ROW_MAPPER,
                            inventoryId
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Inventory> getInventoryByProductId(int productId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "SELECT * FROM Inventory WHERE product_id = ?",
                            INVENTORY_ROW_MAPPER,
                            productId
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Inventory> getAllInventory() {
        return jdbcTemplate.query(
                "SELECT * FROM Inventory ORDER BY inventory_id",
                INVENTORY_ROW_MAPPER
        );
    }

    public int updateInventory(int inventoryId, Inventory inventory) {
        StringBuilder sql = new StringBuilder("UPDATE Inventory SET ");
        List<Object> params = new ArrayList<>();


        if (inventory.getStock_quantity() != 0) {
            sql.append("stock_quantity = ?, ");
            params.add(inventory.getStock_quantity());
        }

        if (inventory.getWarehouse_location() != null) {
            sql.append("warehouse_location = ?, ");
            params.add(inventory.getWarehouse_location());
        }

        if (params.isEmpty()) {
            return 0;
        }

        sql.append("updated_at = NOW()");
        sql.append(" WHERE inventory_id = ?");
        params.add(inventoryId);

        return jdbcTemplate.update(sql.toString(), params.toArray());
    }

    public int adjustStockByProductId(int productId, int quantityDelta) {
        return jdbcTemplate.update(
                """
                UPDATE Inventory
                SET stock_quantity = stock_quantity + ?,
                    updated_at = NOW()
                WHERE product_id = ?
                """,
                quantityDelta,
                productId
        );
    }

    public int deleteInventory(int inventoryId) {
        return jdbcTemplate.update(
                "DELETE FROM Inventory WHERE inventory_id = ?",
                inventoryId
        );
    }
}
