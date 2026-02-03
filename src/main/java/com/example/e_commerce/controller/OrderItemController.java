package com.example.e_commerce.controller;

import com.example.e_commerce.dto.JsonResponseDto.SuccessResponse;
import com.example.e_commerce.model.OrderItem;
import com.example.e_commerce.service.OrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
@AllArgsConstructor
@Tag(name = "Order Items", description = "Items within an order")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping
    public ResponseEntity createOrderItem(@RequestBody OrderItem orderItem) {
        orderItemService.createOrderItem(orderItem);
        SuccessResponse<String> res = new SuccessResponse<>("Order item created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity getOrderItemById(@PathVariable int id) {
        OrderItem item = orderItemService.getOrderItemById(id);
        SuccessResponse<OrderItem> res = new SuccessResponse<>("Order item retrieved successfully", item);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity getOrderItemsByOrderId(@PathVariable int orderId) {
        List<OrderItem> items = orderItemService.getOrderItemsByOrderId(orderId);
        SuccessResponse<List<OrderItem>> res = new SuccessResponse<>("Order items fetched successfully", items);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateOrderItem(@PathVariable int id, @RequestBody OrderItem orderItem) {
        OrderItem updated = orderItemService.updateOrderItem(id, orderItem);
        SuccessResponse<OrderItem> res = new SuccessResponse<>("Order item updated successfully", updated);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteOrderItem(@PathVariable int id) {
        orderItemService.deleteOrderItem(id);
        SuccessResponse<String> res = new SuccessResponse<>("Order item deleted successfully");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping("/order/{orderId}")
    public ResponseEntity deleteOrderItemsByOrderId(@PathVariable int orderId) {
        orderItemService.deleteOrderItemsByOrderId(orderId);
        SuccessResponse<String> res = new SuccessResponse<>("Order items deleted successfully");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
