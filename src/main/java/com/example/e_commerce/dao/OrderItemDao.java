package com.example.e_commerce.dao;

import com.example.e_commerce.model.OrderItem;
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
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<OrderItem> ORDER_ITEM_ROW_MAPPER = (rs, rowNum) ->
            new OrderItem(
                    rs.getInt("order_item_id"),
                    rs.getInt("order_id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    rs.getBigDecimal("unit_price")
            );

    public int createOrderItem(OrderItem orderItem) {
        return jdbcTemplate.update(
                """
                INSERT INTO OrderItem (order_id, product_id, quantity, unit_price)
                VALUES (?, ?, ?, ?)
                """,
                orderItem.getOrder_id(),
                orderItem.getProduct_id(),
                orderItem.getQuantity(),
                orderItem.getUnit_price()
        );
    }

    public Optional<OrderItem> getOrderItemById(int orderItemId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "SELECT * FROM OrderItem WHERE order_item_id = ?",
                            ORDER_ITEM_ROW_MAPPER,
                            orderItemId
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        return jdbcTemplate.query(
                "SELECT * FROM OrderItem WHERE order_id = ? ORDER BY order_item_id",
                ORDER_ITEM_ROW_MAPPER,
                orderId
        );
    }

    public int updateOrderItem(int orderItemId, OrderItem orderItem) {
        StringBuilder sql = new StringBuilder("UPDATE OrderItem SET ");
        List<Object> params = new ArrayList<>();

        if (orderItem.getOrder_id() != 0) {
            sql.append("order_id = ?, ");
            params.add(orderItem.getOrder_id());
        }

        if (orderItem.getProduct_id() != 0) {
            sql.append("product_id = ?, ");
            params.add(orderItem.getProduct_id());
        }

        if (orderItem.getQuantity() != 0) {
            sql.append("quantity = ?, ");
            params.add(orderItem.getQuantity());
        }

        if (orderItem.getUnit_price() != null) {
            sql.append("unit_price = ?, ");
            params.add(orderItem.getUnit_price());
        }

        if (params.isEmpty()) {
            return 0;
        }

        sql.setLength(sql.length() - 2);
        sql.append(" WHERE order_item_id = ?");
        params.add(orderItemId);

        return jdbcTemplate.update(sql.toString(), params.toArray());
    }

    public int deleteOrderItem(int orderItemId) {
        return jdbcTemplate.update(
                "DELETE FROM OrderItem WHERE order_item_id = ?",
                orderItemId
        );
    }

    public int deleteOrderItemsByOrderId(int orderId) {
        return jdbcTemplate.update(
                "DELETE FROM OrderItem WHERE order_id = ?",
                orderId
        );
    }
}
