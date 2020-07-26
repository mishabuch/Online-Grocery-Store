package com.javatpoint.model;

import org.hibernate.validator.constraints.NotBlank;

public class PaymentForm {
	@NotBlank 
	private String paymentMethod;
	
	@NotBlank
	private String cardNumber;

	@NotBlank 
	private String paymentDate;
	
	@NotBlank
	private String deliveryDate;

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

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
}