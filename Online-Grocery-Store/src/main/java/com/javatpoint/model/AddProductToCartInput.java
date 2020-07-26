package com.javatpoint.model;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

public class AddProductToCartInput {
	@Min(value = 1)
	private Integer quantity;
	@NotEmpty
	private int productId;
	
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
}