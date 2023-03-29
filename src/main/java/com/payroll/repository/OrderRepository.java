package com.payroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.payroll.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
