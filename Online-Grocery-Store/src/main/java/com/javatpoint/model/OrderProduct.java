package com.javatpoint.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "order_product")
public class OrderProduct {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "cart_id")
	@JsonBackReference(value="cart-order-product")
	private Cart cart;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	@JsonBackReference(value="xxx")
	private Order order;

	@NotNull
	private int productId;

	@NotEmpty
	private String productName;
	
	@NotNull
	private int productPrice;

	/*
	 * @Min(0) private Integer productStock;
	 */

	@Min(1)
	private Integer count;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}

	/*
	 * public Integer getProductStock() { return productStock; }
	 * 
	 * public void setProductStock(Integer productStock) { this.productStock =
	 * productStock; }
	 */

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public OrderProduct(Product productInfo, Integer quantity) {
		this.productId = productInfo.getProductId();
		this.productName = productInfo.getName();
		this.productPrice = productInfo.getPrice();
		//this.productStock = productInfo.getQuantityInInventory();
		this.count = quantity;
	}
	
	public OrderProduct() {
	
	}

}