package com.javatpoint.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "product")
public class Product {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int productId;
	
	@Column
	@NotNull(message = "Product Name cannot be empty")
	private String name;
	
	@Column
	@NotNull
	@Min(0)
	private float price;
	
	@Column
	@NotNull
	@Min(0)
	private int quantityInInventory;

	@Column
	private String merchant;
	
	@Column
	private String expiryDate; // Haven't kept it mandatory. Can be mandatory 
	
	@Column
	private String unitOfMeasurement; // Haven't kept it mandatory. Can be mandatory 
	
	public String getUnitOfMeasurement() {
		return unitOfMeasurement;
	}

	public void setUnitOfMeasurement(String unit_of_measurement) {
		this.unitOfMeasurement = unit_of_measurement;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Product(String name, int price, int quantityInInventory,String merchant, String expiryDate) {
		this.name = name;
		this.price = price;
		this.quantityInInventory = quantityInInventory;
		this.merchant = merchant;
		this.expiryDate = expiryDate;
		
	}
	
	 public Product() {
	    }
	
	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getQuantityInInventory() {
		return quantityInInventory;
	}

	public void setQuantityInInventory(int quantityInInventory) {
		this.quantityInInventory = quantityInInventory;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}


}