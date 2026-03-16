package com.example.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.OrderItem;

@Repository
public interface OrderrItemRepository extends JpaRepository<OrderItem, Integer>{
@Query("SELECT oi FROM OrderItem oi WHERE oi.order.orderId = :orderId")
List<OrderItem> findByOrderId(String orderId);

@Query("SELECT oi FROM OrderItem oi WHERE oi.order.userId = :userId AND oi.order.status = 'SUCCESS'")
List<OrderItem> findSuccessfulOrderItemsByUserId(int userId);
}
