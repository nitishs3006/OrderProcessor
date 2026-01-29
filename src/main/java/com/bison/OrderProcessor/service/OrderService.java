package com.bison.OrderProcessor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.bison.OrderProcessor.entity.Order;
import com.bison.OrderProcessor.entity.OrderStatus;
import com.bison.OrderProcessor.repository.OrderRepository;

import org.springframework.transaction.annotation.Transactional;


@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryService inventoryService;

    @Transactional
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

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

    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}
