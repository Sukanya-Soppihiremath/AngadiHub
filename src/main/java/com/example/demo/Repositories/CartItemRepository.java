package com.example.demo.Repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.CartItem;
import com.example.demo.Entities.OrderItem;

import org.springframework.data.repository.query.Param;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    
    // Custom query to count cart items for a given userId
    @Query("SELECT COUNT(c) FROM CartItem c WHERE c.user.id = :userId")
    int countCartItemsByUserId(@Param("userId") int userId);

    // Custom query to fetch userId by username (assumes relationship exists)
    @Query("SELECT u.id FROM User u WHERE u.username = :username")
    int findUserIdByUsername(@Param("username") String username);

}
