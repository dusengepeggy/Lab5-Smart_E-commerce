package com.example.e_commerce.service;

import com.example.e_commerce.dao.InventoryDao;
import com.example.e_commerce.model.Inventory;
import com.example.e_commerce.utils.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryDao inventoryDao;

    public void createInventory(Inventory inventory) {
        inventoryDao.createInventory(inventory);
    }

    public Inventory getInventoryById(int inventoryId) {
        return inventoryDao.getInventoryById(inventoryId)
                .orElseThrow(() -> new NotFoundException("Inventory with ID " + inventoryId + " not found"));
    }

    public Inventory getInventoryByProductId(int productId) {
        return inventoryDao.getInventoryByProductId(productId)
                .orElseThrow(() -> new NotFoundException("Inventory for product ID " + productId + " not found"));
    }

    public List<Inventory> getAllInventory() {
        return inventoryDao.getAllInventory();
    }

    public Inventory updateInventory(int inventoryId, Inventory inventory) {
        int rows = inventoryDao.updateInventory(inventoryId, inventory);
        if (rows == 0) {
            throw new NotFoundException("Inventory not found");
        }
        return getInventoryById(inventoryId);
    }

    public void adjustStockByProductId(int productId, int quantityDelta) {
        int rows = inventoryDao.adjustStockByProductId(productId, quantityDelta);
        if (rows == 0) {
            throw new NotFoundException("Inventory for product not found");
        }
    }

    public void deleteInventory(int inventoryId) {
        int rows = inventoryDao.deleteInventory(inventoryId);
        if (rows == 0) {
            throw new NotFoundException("Inventory not found");
        }
    }
}
