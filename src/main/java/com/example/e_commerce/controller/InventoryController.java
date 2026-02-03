package com.example.e_commerce.controller;

import com.example.e_commerce.dto.JsonResponseDto.SuccessResponse;
import com.example.e_commerce.model.Inventory;
import com.example.e_commerce.service.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@AllArgsConstructor
@Tag(name = "Inventory", description = "Stock per product")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity createInventory(@RequestBody Inventory inventory) {
        inventoryService.createInventory(inventory);
        SuccessResponse<String> res = new SuccessResponse<>("Inventory created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity getInventoryById(@PathVariable int id) {
        Inventory inventory = inventoryService.getInventoryById(id);
        SuccessResponse<Inventory> res = new SuccessResponse<>("Inventory retrieved successfully", inventory);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity getInventoryByProductId(@PathVariable int productId) {
        Inventory inventory = inventoryService.getInventoryByProductId(productId);
        SuccessResponse<Inventory> res = new SuccessResponse<>("Inventory retrieved successfully", inventory);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping
    public ResponseEntity getAllInventory() {
        List<Inventory> list = inventoryService.getAllInventory();
        SuccessResponse<List<Inventory>> res = new SuccessResponse<>("Inventory fetched successfully", list);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateInventory(@PathVariable int id, @RequestBody Inventory inventory) {
        Inventory updated = inventoryService.updateInventory(id, inventory);
        SuccessResponse<Inventory> res = new SuccessResponse<>("Inventory updated successfully", updated);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PatchMapping("/product/{productId}/adjust")
    public ResponseEntity adjustStock(@PathVariable int productId, @RequestParam int delta) {
        inventoryService.adjustStockByProductId(productId, delta);
        SuccessResponse<String> res = new SuccessResponse<>("Stock adjusted successfully");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteInventory(@PathVariable int id) {
        inventoryService.deleteInventory(id);
        SuccessResponse<String> res = new SuccessResponse<>("Inventory deleted successfully");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
