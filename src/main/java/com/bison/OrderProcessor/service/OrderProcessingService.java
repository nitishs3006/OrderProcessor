package com.bison.OrderProcessor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.bison.OrderProcessor.entity.Order;
import com.bison.OrderProcessor.entity.OrderStatus;
import com.bison.OrderProcessor.repository.OrderRepository;

@Service
public class OrderProcessingService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryService inventoryService;

    @Async("orderExecutor")
    public void processOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        try {
            System.out.println("Processing order in thread: " + Thread.currentThread().getName());
            inventoryService.reduceStockAndCompleteOrder(order);
        } catch (Exception e) {
            order.setStatus(OrderStatus.FAILED);
            orderRepository.save(order);
            System.out.println("Order failed: " + e.getMessage());
        }
    }
}
