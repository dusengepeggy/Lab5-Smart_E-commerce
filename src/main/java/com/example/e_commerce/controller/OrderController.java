package com.example.e_commerce.controller;

import com.example.e_commerce.dto.JsonResponseDto.SuccessResponse;
import com.example.e_commerce.model.Order;
import com.example.e_commerce.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
@Tag(name = "Orders", description = "Order create, read, update, delete")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity createOrder(@RequestBody Order order) {
        orderService.createOrder(order);
        SuccessResponse<String> res = new SuccessResponse<>("Order created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity getOrderById(@PathVariable int id) {
        Order order = orderService.getOrderById(id);
        SuccessResponse<Order> res = new SuccessResponse<>("Order retrieved successfully", order);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping
    public ResponseEntity getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        SuccessResponse<List<Order>> res = new SuccessResponse<>("Orders fetched successfully", orders);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity getOrdersByUserId(@PathVariable int userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        SuccessResponse<List<Order>> res = new SuccessResponse<>("Orders fetched successfully", orders);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/status")
    public ResponseEntity getOrdersByStatus(@RequestParam String status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        SuccessResponse<List<Order>> res = new SuccessResponse<>("Orders fetched successfully", orders);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateOrder(@PathVariable int id, @RequestBody Order order) {
        Order updated = orderService.updateOrder(id, order);
        SuccessResponse<Order> res = new SuccessResponse<>("Order updated successfully", updated);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteOrder(@PathVariable int id) {
        orderService.deleteOrder(id);
        SuccessResponse<String> res = new SuccessResponse<>("Order deleted successfully");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
