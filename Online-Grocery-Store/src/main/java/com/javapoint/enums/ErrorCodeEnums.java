package com.javapoint.enums;

public enum ErrorCodeEnums {

	// Error codes and messages 
	COULD_NOT_SAVE_USER(2, "Could Not Save User due to some Internel Server Error."),
	INVALID_DATE_FORMAT(28, "Enter Date in yyyy-mm-dd format"),
	CANNOT_ADD_EXPIRED_PRODUCT_TO_INVENTORY(299, "Cannot add an expired product to inventory"),
	CANNOT_ADD_EXPIRED_PRODUCT_TO_CART(300, "Cannot add an expired product to cart"),
	ORDER_NOT_PAID_FOR(55, "Order Not Payed for. Please finish payment and try again."),
	CANNOT_CANCEL(56, "Cannot cancel an arder which is not placed"),
	INVALID_STATUS(29, "Order is cancelled, placed or already paid for."),
	COULD_NOT_SAVE_PRODUCT(22, "Could Not Save Product due to some Internel Server Error."),
	COULD_NOT_DELETE_USER(100, "Could Not Delete User due to some Internel Server Error."),
	COULD_NOT_DELETE_PRODUCT(110, "Could Not Product User due to some Internel Server Error."),
	PRODUCT_NOT_EXIST(3, "Product does not exist."), 
	NO_RESULTS_FOUND(192, "No result found for this search"), 
	PRODUCT_NOT_ENOUGH(4, "Not enough Product in Stock"),
	PRODUCT_ALREADY_EXISTS(41, "There is already a product with this Id. Could not create product"),
	CART_CHECKOUT_SUCCESS(5, "Checkout successfully! "),
	ORDER_NOT_FOUND(6, "Order does not exist"), 
	UNAUTHORIZED_ACCESS(9, "You are not authorized to access this resource"),
	USER_DOES_NOT_EXIST(10, "User does not exist"), 
	PASSWORD_MISMATCH(11, "Invalid password. Enter Again"),
	EMAIL_MISSING(13, "Please Enter Email"),
	PASSWORD_MISSING(14, "Please Enter Password"),
	ADDRESS_MISSING(14, "Please Enter Address"),
	USERNAME_MISSING(15, "Please Enter UserName"),
	COULD_NOT_ADD_BACK_STOCK(16, "Could not restock after cancelling order"),
	USERNAME_PASSWORD_MISSING(12, "Enter UserName And Password> Neither can be empty"),
	COULD_NOT_ADD_PRODUCT_TO_CART(99,"Coud not add to cart"),
	EDIT_CART_FAILED(199,"Edit Cart Failed"),
	CART_IS_EMPTY(201,"Cart is empty. Cannot create order"),
	TOKEN_NOT_PASSED(202,"Pass a valid token to access this api"),
	INVALID_DELIVERY_DATE(203,"Please select a delivery date after Order Date"),
	CHECKOUT_FAILED(200,"Checkout Failed");

	private int code;

	private String message;

	public Integer getCode() {
		return code;
	}


	public String getMessage() {
		return message;
	}

	ErrorCodeEnums(int code, String message) {
		this.code = code;
		this.message = message;
	}
}