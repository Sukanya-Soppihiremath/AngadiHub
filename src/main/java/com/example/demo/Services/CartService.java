package com.example.demo.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entities.CartItem;
import com.example.demo.Entities.Product;
import com.example.demo.Entities.ProductImage;
import com.example.demo.Entities.User;
import com.example.demo.Repositories.CartRepository;
import com.example.demo.Repositories.ProductImageRepository;
import com.example.demo.Repositories.ProductRepository;
import com.example.demo.Repositories.UserRepository;

@Service
public class CartService {

	@Autowired
	CartRepository cartrepo;
	
	@Autowired
	UserRepository userrepo;
	@Autowired
	ProductRepository prorepo;
	
	@Autowired
	ProductImageRepository proimagerepo;

	public int getCartItemCount(int userId) {
		return cartrepo.countTotalItems(userId);
	}
	public void addToCart(int userId, int productId, int quantity) {
		User user = userrepo.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

		Product product = prorepo.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
	    
	  Optional<CartItem> existingItem =   cartrepo.findByUserAndProduct(userId, productId);
	  
	  if(existingItem.isPresent()) {
		  CartItem cartitem = existingItem.get();
		  cartitem.setQuantity(cartitem.getQuantity() + quantity);
		  cartrepo.save(cartitem);
	  } else {
		  CartItem newitem = new CartItem(user, product, quantity);
		  cartrepo.save(newitem);
	  }
	}
	
	public Map<String, Object> getCartItems(int userId) {
		// Fetch the cart items for the user with product details
		List<CartItem> cartItems = cartrepo.findCartItemsWithProductDetails1(userId);
		
		Map<String, Object>  response = new HashMap<>();
		User user = userrepo.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		response.put("username", user.getUsername());
		response.put("role", user.getRole().toString());
	
	List<Map<String, Object>> products = new ArrayList<>();
	int overAllTotalPrice = 0;
	
	for(CartItem cartitem : cartItems) {
		Map<String, Object> productDetails = new HashMap<>();
		
		Product product = cartitem.getProduct();
		
		List<ProductImage> productimage = proimagerepo.findByProduct_ProductId(product.getProductId());
	String imgeUrl = null;
	
	if (productimage != null && !productimage.isEmpty()) {
		// If there are images, get the first image's URL
		imgeUrl = productimage.get(0).getImageUrl();
	} else {
		// Set a default image if no images are available
		imgeUrl = "default-image-url";  // You can replace this with your default image URL
	}
	
	
	productDetails.put("productId", product.getProductId());
	productDetails.put("image_url", imgeUrl);
	productDetails.put("name", product.getName());
	productDetails.put("description", product.getDescription());
	productDetails.put("price_per_unit", product.getPrice());
	productDetails.put("quantity", cartitem.getQuantity());
	productDetails.put("total_price", cartitem.getQuantity() * product.getPrice().doubleValue());
	
	products.add(productDetails);
	overAllTotalPrice += cartitem.getQuantity() * product.getPrice().doubleValue();
	
	}
	Map<String, Object> cart = new HashMap<>();
	cart.put("products", products);
	cart.put("overall_total_price", overAllTotalPrice);
	response.put("Cart", cart);
	return response;
	}
	public void updateCartItemQuantity(int userId, int productId, int quantity) {
		User user = userrepo.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		Product product = prorepo.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));

		// Fetch cart item for this userId and productId
		Optional<CartItem> existingItem = cartrepo.findByUserAndProduct(userId, productId);

		if (existingItem.isPresent()) {
			CartItem cartItem = existingItem.get();
			if (quantity == 0) {
				deleteCartItem(userId, productId);
			} else {
				cartItem.setQuantity(quantity);
				cartrepo.save(cartItem);
			}
		}
	}

	// Delete Cart Item
	public void deleteCartItem(int userId, int productId) {
		User user = userrepo.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		Product product = prorepo.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));

		cartrepo.deleteCartItem(userId, productId);
	}
}
