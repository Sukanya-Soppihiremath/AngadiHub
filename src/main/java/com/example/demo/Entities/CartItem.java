package com.example.demo.Entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart_items")
public class CartItem {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
private Integer id;
	@ManyToOne
	@JoinColumn(name="user_id",nullable=false)
private User user;
	@ManyToOne
	@JoinColumn(name="product_id",nullable=false)
private Product product;
	
	int quantity;

public CartItem() {
	// TODO Auto-generated constructor stub
}



public CartItem(int id, User user, Product product, int quantity) {
	super();
	this.id = id;
	this.user = user;
	this.product = product;
	this.quantity = quantity;
}



public CartItem(User user, Product product, int quantity) {
	this.user = user;
	this.product = product;
	this.quantity = quantity;
}



public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public User getUser() {
	return user;
}

public void setUser(User iser) {
	this.user = iser;
}

public Product getProduct() {
	return product;
}

public void setProduct(Product product) {
	this.product = product;
}



public int getQuantity() {
	return quantity;
}



public void setQuantity(int quantity) {
	this.quantity = quantity;
}


}
