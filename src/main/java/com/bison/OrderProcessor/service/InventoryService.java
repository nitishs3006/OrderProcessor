package com.bison.OrderProcessor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bison.OrderProcessor.entity.Inventory;
import com.bison.OrderProcessor.entity.Order;
import com.bison.OrderProcessor.entity.OrderStatus;
import com.bison.OrderProcessor.repository.InventoryRepository;
import com.bison.OrderProcessor.repository.OrderRepository;

import org.springframework.transaction.annotation.Transactional;


@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Transactional
    public Inventory createInventory(Long productId, int initialStock) {

        if (inventoryRepository.existsById(productId)) {
            throw new RuntimeException("Inventory already exists for product " + productId);
        }

        Inventory inventory = new Inventory(productId, initialStock);
        return inventoryRepository.save(inventory);
    }
    
    @Transactional
    public void reduceStockAndCompleteOrder(Order order) {
        Inventory inventory = inventoryRepository
                .findByProductIdForUpdate(order.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (inventory.getStock() < order.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        inventory.setStock(inventory.getStock() - order.getQuantity());
        inventoryRepository.save(inventory);

        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
    }
    
    

}