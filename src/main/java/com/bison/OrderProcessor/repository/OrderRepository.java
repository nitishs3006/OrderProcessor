package com.bison.OrderProcessor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bison.OrderProcessor.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
