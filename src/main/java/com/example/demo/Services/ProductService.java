package com.example.demo.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entities.Product;
import com.example.demo.Entities.Category;
import com.example.demo.Entities.ProductImage;
import com.example.demo.Repositories.CategoryRepository;
import com.example.demo.Repositories.ProductImageRepository;
import com.example.demo.Repositories.ProductRepository;

@Service
public class ProductService {
@Autowired
private ProductRepository prorepo;
@Autowired
private ProductImageRepository proimagerepo;
@Autowired
private CategoryRepository categoryrepo;

public List<Product> getProductsByCategory(String CategoryName) {
if(CategoryName != null && CategoryName.isEmpty()) {
	Optional<Category> categoryOpt = categoryrepo.findByCategoryName(CategoryName);
	if(categoryOpt.isPresent()) {
		Category category = categoryOpt.get();
		return prorepo.findByCategory_CategoryId(category.getCategoryId());
	} else {
		throw new RuntimeException("Category not found");
	}
} else {
	return prorepo.findAll();
}
}
public List<String> getProductImages(Integer productId) {
	List<ProductImage> proimage = proimagerepo.findByProduct_ProductId(productId);
	List<String> imageUrls = new ArrayList<>();
	for(ProductImage image : proimage) {
		imageUrls.add(image.getImageUrl());
	}
	return imageUrls;
}
}
