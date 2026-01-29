package com.example.e_commerce.dao;

import com.example.e_commerce.model.Order;
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
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Order> ORDER_ROW_MAPPER = (rs, rowNum) ->
            new Order(
                    rs.getInt("order_id"),
                    rs.getInt("user_id"),
                    rs.getDate("order_date"),
                    rs.getBigDecimal("total_amount"),
                    rs.getString("status")
            );

    public int createOrder(Order order) {
        return jdbcTemplate.update(
                """
                INSERT INTO "Order" (user_id, order_date, total_amount, status)
                VALUES (?, COALESCE(?, CURRENT_DATE), ?, ?)
                """,
                order.getUser_id(),
                order.getOrder_date(),
                order.getTotal_amount(),
                order.getStatus()
        );
    }

    public Optional<Order> getOrderById(int orderId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "SELECT * FROM \"Order\" WHERE order_id = ?",
                            ORDER_ROW_MAPPER,
                            orderId
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Order> getAllOrders() {
        return jdbcTemplate.query(
                "SELECT * FROM \"Order\" ORDER BY order_date DESC, order_id DESC",
                ORDER_ROW_MAPPER
        );
    }

    public List<Order> getOrdersByUserId(int userId) {
        return jdbcTemplate.query(
                "SELECT * FROM \"Order\" WHERE user_id = ? ORDER BY order_date DESC, order_id DESC",
                ORDER_ROW_MAPPER,
                userId
        );
    }

    public List<Order> getOrdersByStatus(String status) {
        return jdbcTemplate.query(
                "SELECT * FROM \"Order\" WHERE status = ? ORDER BY order_date DESC, order_id DESC",
                ORDER_ROW_MAPPER,
                status
        );
    }

    public int updateOrder(int orderId, Order order) {
        StringBuilder sql = new StringBuilder("UPDATE \"Order\" SET ");
        List<Object> params = new ArrayList<>();

        if (order.getOrder_date() != null) {
            sql.append("order_date = ?, ");
            params.add(order.getOrder_date());
        }

        if (order.getTotal_amount() != null) {
            sql.append("total_amount = ?, ");
            params.add(order.getTotal_amount());
        }

        if (order.getStatus() != null) {
            sql.append("status = ?, ");
            params.add(order.getStatus());
        }

        if (params.isEmpty()) {
            return 0;
        }

        sql.setLength(sql.length() - 2);
        sql.append(" WHERE order_id = ?");
        params.add(orderId);

        return jdbcTemplate.update(sql.toString(), params.toArray());
    }

    public int deleteOrder(int orderId) {
        return jdbcTemplate.update(
                "DELETE FROM \"Order\" WHERE order_id = ?",
                orderId
        );
    }
}
