package com.javapoint.test.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.javapoint.enums.StatusEnum;
import com.javatpoint.model.AddProductToCartInput;
import com.javatpoint.model.Cart;
import com.javatpoint.model.LoginForm;
import com.javatpoint.model.Order;
import com.javatpoint.model.OrderProduct;
import com.javatpoint.model.PaymentForm;
import com.javatpoint.model.Product;
import com.javatpoint.model.User;

public class TestUtils {
	
	
	public static Product createValidProduct() {
		Product product = new Product();
		product.setMerchant("unilever");
		product.setName("tea");
		product.setExpiryDate("2020-10-01");
		product.setPrice(100);
		product.setProductId(1);
		product.setQuantityInInventory(100);
		return product;
	}
	
	public static Cart createValidCart() {
		Cart cart = new Cart();
		cart.setCartId(1);
		Set<OrderProduct> productsInOrder = new HashSet<>();
		OrderProduct op = new OrderProduct();
		op.setCount(1);
		op.setId(1);
		op.setProductId(1);
		op.setProductName("tea");
		op.setProductPrice(100);
		Order order = new Order();
		order.setOrderId(1);
		op.setOrder(order);
		productsInOrder.add(op);
		cart.setProductsInOrder(productsInOrder);
		return cart;
	}
	
	public static AddProductToCartInput getAddProductForm() {
		AddProductToCartInput  form = new AddProductToCartInput();
		form.setProductId(1);
		form.setQuantity(1);
		return form;
	}
	
	public static PaymentForm getPaymentInfo() {
		PaymentForm pf = new PaymentForm();
		pf.setDeliveryDate("2021-10-10");
		pf.setPaymentMethod("card");
		pf.setPaymentDate("2020-07-26");
		pf.setCardNumber("943987");
		return pf;
	}
	
	public static User createValidUser() {
		User user = new User();
		user.setUserName("User1");
		user.setUserId(1);
		user.setPhone(9663507);
		user.setPassword("password");
		user.setIsAdmin(1);
		user.setEmail("user1@gmail.com");
		user.setAddress("India");
		user.setCart(createValidCart());
		return user;

	}
	
	public static Order createValidOrder() {
		Order order = new Order();
		order.setAddress("India");
		order.setCardNumber("123");
		order.setDeliveryDate("2020-11-01");
		order.setEmail("abc@gamil.com");
		order.setOrderDate("2020-10-09");
		order.setOrderId(1);
		order.setPaymentDate("2020-10-10");
		order.setPhone(9888);
		order.setStatus(StatusEnum.CREATED.getMsg());
		order.setTotalValue(100);
		order.setTransactionID(11133);
		order.setUserId(1);
		return order;
	}
	

	public static ResponseEntity<Order> createValidCancelledOrder() {
		Order order = new Order();
		order.setAddress("India");
		order.setCardNumber("123");
		order.setDeliveryDate("2020-11-01");
		order.setEmail("abc@gamil.com");
		order.setOrderDate("2020-10-09");
		order.setOrderId(1);
		order.setPaymentDate("2020-10-10");
		order.setPhone(9888);
		order.setStatus(StatusEnum.CANCELED.getMsg());
		order.setTotalValue(100);
		order.setTransactionID(11133);
		order.setUserId(1);
		return ResponseEntity.status(HttpStatus.OK).body(order);
	}

	public static List<Order> getOrderlist(){
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(createValidOrder());
		return orderList;
		
	}
	
	public static Object createProductsList() {
		List<Product> products = new ArrayList<>();
		products.add(createValidProduct());
		return products;
	}
	
	public static Object emptyProductList() {
		List<Product> products = new ArrayList<>();
		return products;
	}
	


	
	public static LoginForm createLoginForm() {
		LoginForm login = new LoginForm();
		login.setUsername("User1");
		login.setPassword("password");
		return login;
	}


	public static Object createUsersList() {
		List<User> users = new ArrayList<>();
		users.add(createValidUser());
		return users;
	}


}
