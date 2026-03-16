package com.example.demo.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.CartItem;

import jakarta.transaction.Transactional;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Integer> {
	  @Query("SELECT c FROM CartItem c WHERE c.user.userId = :userId AND c.product.productId = :productId")
	  Optional<CartItem> findByUserAndProduct(@Param("userId") int userId,
              @Param("productId") int productId);
	  
	  @Query("SELECT c FROM CartItem c JOIN FETCH c.product p LEFT JOIN FETCH ProductImage pi ON p.productId = pi.product.productId WHERE c.user.userId = :userId")
	    List<CartItem> findCartItemsWithProductDetails1(int userId);
	  
	  @Query("UPDATE CartItem c SET c.quantity = :quantity WHERE c.id = :cartItemId")
	    void updateCartItemQuantity(int cartItemId, int quantity);
	  
	    @Modifying
	    @Transactional
	    @Query("DELETE FROM CartItem c WHERE c.user.userId = :userId AND c.product.productId = :productId")
	    void deleteCartItem(int userId, int productId);
	    
	    @Query("SELECT COALESCE(SUM(c.quantity), 0) FROM CartItem c WHERE c.user.userId = :userId")
	    int countTotalItems(int userId);

	    
	    @Modifying
	    @Transactional
	    @Query("DELETE FROM CartItem c WHERE c.user.userId = :userId")
	    void deleteAllCartItemsByUserId(int userId);

}
