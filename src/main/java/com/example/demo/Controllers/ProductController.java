package com.example.demo.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entities.Product;
import com.example.demo.Entities.User;
import com.example.demo.Services.ProductService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins="http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/products")
public class ProductController {
@Autowired
private ProductService proservice;
@GetMapping
public ResponseEntity<Map<String, Object>> getProduct(
		@RequestParam(required = false) String category, HttpServletRequest request
		) {
	try {
		User authenticattedUser = (User) request.getAttribute("authenticatedUser");
		if(authenticattedUser == null) {
			return ResponseEntity.status(401).body(Map.of("error", "Unauthorized access"));
		}
		List<Product> products = proservice.getProductsByCategory(category);
		Map<String, Object> response = new HashMap<>();
		Map<String, String> userInfo = new HashMap<>();
		userInfo.put("name", authenticattedUser.getUsername());
		userInfo.put("role", authenticattedUser.getRole().name());
		response.put("user", userInfo);
		
		List<Map<String, Object>> proList = new ArrayList<>();
		for(Product pr : products) {
			Map<String, Object> proDetails = new HashMap<>();
			proDetails.put("product_id", pr.getProductId());
			proDetails.put("name", pr.getName());
			proDetails.put("description", pr.getDescription());
			proDetails.put("price", pr.getPrice());
			proDetails.put("stock", pr.getStock());
			
			List<String> images = proservice.getProductImages(pr.getProductId());
			proDetails.put("images", images);
			proList.add(proDetails);
		} response.put("products", proList);
		return ResponseEntity.ok(response);
	} catch (RuntimeException e) {
		return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
	}
}
}
