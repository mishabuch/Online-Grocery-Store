package com.javatpoint.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.javapoint.enums.StatusEnum;

@Entity
@Table(name = "orders")
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int orderId;

	@Column
	private int userId;

	@Column
	private int phone;

	@Column
	private String email;

	@Column
	private String address;
	
	@Column 
	private String paymentMethod;
	

	@Column
	private String cardNumber;

	@Column 
	private String paymentDate;
	
	@Column 
	private double transactionID; 
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
	@JsonManagedReference(value = "xxx")
	private Set<OrderProduct> orderProduct = new HashSet<>();

	@Column
	@NotNull(message = "Order status cannot be empty")
	private String status;

	@Column
	@Min(0)
	private float totalValue;

	@Column
	private String orderDate;

	@Column
	private String deliveryDate;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Set<OrderProduct> getOrderProduct() {
		return orderProduct;
	}

	public void setOrderProduct(Set<OrderProduct> orderProduct) {
		this.orderProduct = orderProduct;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public float getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(int totalValue) {
		this.totalValue = totalValue;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public double getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(double d) {
		this.transactionID = d;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public Order(User user,String orderDate) {
		Set<OrderProduct> products = user.getCart().getProductsInOrder();
		float total = 0;
		for (OrderProduct product : products)
			total = total + (product.getProductPrice()) * product.getCount();
		this.totalValue = total;
		this.userId = user.getUserId();
		this.address = user.getAddress();
		this.email = user.getEmail();
		this.phone = user.getPhone();
		this.status = StatusEnum.CREATED.getMsg();
		this.orderDate = orderDate;

	}

	public Order() {

	}

}