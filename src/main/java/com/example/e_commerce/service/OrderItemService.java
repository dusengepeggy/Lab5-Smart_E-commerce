package com.example.e_commerce.service;

import com.example.e_commerce.dao.OrderItemDao;
import com.example.e_commerce.model.OrderItem;
import com.example.e_commerce.utils.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemDao orderItemDao;

    public void createOrderItem(OrderItem orderItem) {
        orderItemDao.createOrderItem(orderItem);
    }

    public OrderItem getOrderItemById(int orderItemId) {
        return orderItemDao.getOrderItemById(orderItemId)
                .orElseThrow(() -> new NotFoundException("Order item with ID " + orderItemId + " not found"));
    }

    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        return orderItemDao.getOrderItemsByOrderId(orderId);
    }

    public OrderItem updateOrderItem(int orderItemId, OrderItem orderItem) {
        int rows = orderItemDao.updateOrderItem(orderItemId, orderItem);
        if (rows == 0) {
            throw new NotFoundException("Order item not found");
        }
        return getOrderItemById(orderItemId);
    }

    public void deleteOrderItem(int orderItemId) {
        int rows = orderItemDao.deleteOrderItem(orderItemId);
        if (rows == 0) {
            throw new NotFoundException("Order item not found");
        }
    }

    public void deleteOrderItemsByOrderId(int orderId) {
        orderItemDao.deleteOrderItemsByOrderId(orderId);
    }
}
