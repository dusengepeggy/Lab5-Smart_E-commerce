package com.example.e_commerce.service;

import com.example.e_commerce.dao.OrderDao;
import com.example.e_commerce.model.Order;
import com.example.e_commerce.utils.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderDao orderDao;

    public void createOrder(Order order) {
        orderDao.createOrder(order);
    }

    public Order getOrderById(int orderId) {
        return orderDao.getOrderById(orderId)
                .orElseThrow(() -> new NotFoundException("Order with ID " + orderId + " not found"));
    }

    public List<Order> getAllOrders() {
        return orderDao.getAllOrders();
    }

    public List<Order> getOrdersByUserId(int userId) {
        return orderDao.getOrdersByUserId(userId);
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderDao.getOrdersByStatus(status);
    }

    public Order updateOrder(int orderId, Order order) {
        int rows = orderDao.updateOrder(orderId, order);
        if (rows == 0) {
            throw new NotFoundException("Order not found");
        }
        return getOrderById(orderId);
    }

    public void deleteOrder(int orderId) {
        int rows = orderDao.deleteOrder(orderId);
        if (rows == 0) {
            throw new NotFoundException("Order not found");
        }
    }
}
