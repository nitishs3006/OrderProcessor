package com.bison.OrderProcessor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bison.OrderProcessor.entity.Order;
import com.bison.OrderProcessor.entity.OrderStatus;
import com.bison.OrderProcessor.repository.OrderRepository;
import com.bison.OrderProcessor.service.OrderProcessingService;
import com.bison.OrderProcessor.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProcessingService orderProcessingService;
    
    @Autowired
    private OrderService orderService;
    
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        order.setStatus(OrderStatus.PENDING);
        Order saved = orderRepository.save(order);

        orderProcessingService.processOrder(saved.getId());

        return ResponseEntity.ok(saved);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderStatus(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }
}
