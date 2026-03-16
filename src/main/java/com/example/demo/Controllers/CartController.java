package com.example.demo.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entities.User;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Services.CartService;

//import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials= "true")
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	CartService cartservice;
	
	@Autowired
	UserRepository userrepo;
	
	
	@GetMapping("/items/count")
	public ResponseEntity<Map<String, Object>> getCartCount(@RequestParam String username) {
		  User user = userrepo.findByUsername(username)
	                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

	        // Call the service to get the total cart item count
		    Map<String, Object> count = cartservice.getCartItems(user.getUserId());
	        return ResponseEntity.ok(count);
	}
//
//	@GetMapping("/items")
//	public ResponseEntity<Map<String, Object>> getCartItems(HttpServletRequest request) {
//		User user = (User) request.getAttribute("authenticatedUser");
//		
////		 User user = userrepo.findByUsername(username)
////	             .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
//		Map<String, Object> cartItems = cartservice.getCartItems(user.getUserId());
//		return ResponseEntity.ok(cartItems);
//	}
	
	@GetMapping("/items")
	public ResponseEntity<Map<String, Object>> getCartItems(@RequestParam String username) {
	    User user = userrepo.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("Username not found"));
	    
	    Map<String, Object> cartItems = cartservice.getCartItems(user.getUserId());
	    return ResponseEntity.ok(cartItems);
	}
	
	   @PostMapping("/add")
	    @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
	    public ResponseEntity<Void> addToCart1(@RequestBody Map<String, Object> request) {
	        String username = (String) request.get("username");
	        int productId = (int) request.get("productId");

	        // Handle quantity: Default to 1 if not provided
	        int quantity = request.containsKey("quantity") ? (int) request.get("quantity") : 1;

	        // Fetch the user using username
	        User user = userrepo.findByUsername(username)
	                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

	        // Add the product to the cart
	        cartservice.addToCart(user.getUserId(), productId, quantity);
	        return ResponseEntity.status(HttpStatus.CREATED).build();
	    }
	   
	    @PutMapping("/update")
	    public ResponseEntity<Void> updateCartItemQuantity(@RequestBody Map<String, Object> request) {
	        String username = (String) request.get("username");
	        int productId = (int) request.get("productId");
	        int quantity = (int) request.get("quantity");

	        // Fetch the user using username
	        User user = userrepo.findByUsername(username)
	                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

	        // Update the cart item quantity
	        cartservice.updateCartItemQuantity(user.getUserId(), productId, quantity);
	        return ResponseEntity.status(HttpStatus.OK).build();
	    }
	    
	    @DeleteMapping("/delete")
	    public ResponseEntity<Void> deleteCartItem(@RequestBody Map<String, Object> request) {
	        String username = (String) request.get("username");
	        int productId = (int) request.get("productId");

	        // Fetch the user using username
	        User user = userrepo.findByUsername(username)
	                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

	        // Delete the cart item
	        cartservice.deleteCartItem(user.getUserId(), productId);
	        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	    }

}
