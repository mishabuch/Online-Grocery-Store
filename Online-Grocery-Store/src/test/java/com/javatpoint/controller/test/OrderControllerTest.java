package com.javatpoint.controller.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.javapoint.enums.ErrorCodeEnums;
import com.javapoint.enums.StatusEnum;
import com.javatpoint.controller.OrderController;
import com.javatpoint.model.Order;
import com.javatpoint.model.PaymentForm;
import com.javatpoint.model.User;
import com.javatpoint.service.OrderService;
import com.javatpoint.service.UserService;

public class OrderControllerTest {
	
	@InjectMocks
	private OrderController orderController;
	
	@Mock
	private UserService userService;
	
	@Mock
	private OrderService orderService;
	
	@Before
	public void setUp() {
		
		orderController = new OrderController();
		MockitoAnnotations.initMocks(this);
		Mockito.doReturn(createValidOrder()).when(orderService).saveUpdate(Mockito.anyObject());
		Mockito.doReturn(createValidCancelledOrder()).when(orderService).cancelOrder(Mockito.anyInt());
		Mockito.doReturn(getOrderlist()).when(orderService).findByUserEmail(Mockito.anyString());
		Mockito.doReturn(createValidOrder()).when(orderService).findByOrderId(Mockito.anyInt());
		Mockito.doReturn(createValidUser()).when(userService).getUserById(Mockito.anyInt());
	}
	
	@Test
	public void testPositiveGetOrdersList() {
		ResponseEntity<?> rt = orderController.orderList("MQ==");
		Assert.assertEquals(HttpStatus.OK, rt.getStatusCode());
	}
	
	@Test
	public void testNegativeGetOrdersListTokenEmpty() {
		ResponseEntity<?> rt = orderController.orderList("");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeGetOrdersListTokenNull() {
		ResponseEntity<?> rt = orderController.orderList(null);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeGetOrdersListUserNotExists() {
		Mockito.doReturn(null).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = orderController.orderList("MQ==");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testPositiveGetOrderDetail() {
		ResponseEntity<?> rt = orderController.getOrderDetails(1,"MQ==");
		Assert.assertEquals(HttpStatus.OK, rt.getStatusCode());
	}
	
	@Test
	public void testNegativeGetOrderDetailTokenEmpty() {
		ResponseEntity<?> rt = orderController.getOrderDetails(1,"");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeGetOrderDetailTokenNull() {
		ResponseEntity<?> rt = orderController.getOrderDetails(1,null);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeGetOrderDetailOrderNotExist() {
		Mockito.doReturn(null).when(orderService).findByOrderId(Mockito.anyInt());
		ResponseEntity<?> rt = orderController.getOrderDetails(1,"MQ==");
		Assert.assertEquals(HttpStatus.NOT_FOUND, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.ORDER_NOT_FOUND.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeGetOrderUserNotExists() {
		Mockito.doReturn(null).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = orderController.getOrderDetails(1,"MQ==");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeGetOrderUserMismatch() {
		User user = createValidUser();
		user.setUserId(2);
		Mockito.doReturn(user).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = orderController.getOrderDetails(1,"MQ==");
		Assert.assertEquals(HttpStatus.UNAUTHORIZED, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.UNAUTHORIZED_ACCESS.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testPositiveCancelOrder() {
		ResponseEntity<?> rt = orderController.cancelOrder(1, "MQ==");
		Assert.assertEquals(HttpStatus.OK, rt.getStatusCode());
	}
	
	@Test
	public void testPositiveCancelOrderUserMismatch() {
		User user = createValidUser();
		user.setUserId(2);
		Mockito.doReturn(user).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = orderController.cancelOrder(1, "MQ==");
		Assert.assertEquals(HttpStatus.UNAUTHORIZED, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.UNAUTHORIZED_ACCESS.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeCancelOrderDetailOrderNotExist() {
		Mockito.doReturn(null).when(orderService).findByOrderId(Mockito.anyInt());
		ResponseEntity<?> rt = orderController.cancelOrder(1,"MQ==");
		Assert.assertEquals(HttpStatus.NOT_FOUND, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.ORDER_NOT_FOUND.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeCancelOrderDetailUserNotExist() {
		Mockito.doReturn(null).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = orderController.cancelOrder(1,"MQ==");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeCancelOrderDetailTokenEmpty() {
		ResponseEntity<?> rt = orderController.cancelOrder(1,"");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeCancelOrderDetailTokenNull() {
		ResponseEntity<?> rt = orderController.cancelOrder(1,null);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testPositivePayForOrder() {
		PaymentForm pf = getPaymentInfo();
		ResponseEntity<?> rt = orderController.payForOrder(1, "MQ==", pf);
		Assert.assertEquals(HttpStatus.OK, rt.getStatusCode());
	}
	
	@Test
	public void testNegativePayForOrderInvalidOrderStatus() {
		PaymentForm pf = getPaymentInfo();
		Order order = createValidOrder();
		order.setStatus(StatusEnum.PLACED.getMsg());
		Mockito.doReturn(order).when(orderService).findByOrderId(Mockito.anyInt());
		ResponseEntity<?> rt = orderController.payForOrder(1, "MQ==", pf);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.INVALID_STATUS.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativePayForOrderUserMismatch() {
		PaymentForm pf = getPaymentInfo();
		User user = createValidUser();
		user.setUserId(2);
		Mockito.doReturn(user).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = orderController.payForOrder(1, "MQ==", pf);
		Assert.assertEquals(HttpStatus.UNAUTHORIZED, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.UNAUTHORIZED_ACCESS.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativePayForOrderUserNull() {
		PaymentForm pf = getPaymentInfo();
		Mockito.doReturn(null).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = orderController.payForOrder(1, "MQ==", pf);
		Assert.assertEquals(HttpStatus.NOT_FOUND, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativePayForOrderTokenNull() {
		PaymentForm pf = getPaymentInfo();
		ResponseEntity<?> rt = orderController.payForOrder(1, null, pf);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
	}

	@Test
	public void testNegativePayForOrderTokenEmpty() {
		PaymentForm pf = getPaymentInfo();
		ResponseEntity<?> rt = orderController.payForOrder(1, "", pf);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativePayForOrderDetailOrderNotExist() {
		PaymentForm pf = getPaymentInfo();
		Mockito.doReturn(null).when(orderService).findByOrderId(Mockito.anyInt());
		ResponseEntity<?> rt = orderController.payForOrder(1,"MQ==",pf);
		Assert.assertEquals(HttpStatus.NOT_FOUND, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.ORDER_NOT_FOUND.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativePayForOrderDetailDelivertDateInvalid() {
		PaymentForm pf = getPaymentInfo();
		pf.setDeliveryDate("112weewe");
		ResponseEntity<?> rt = orderController.payForOrder(1,"MQ==",pf);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.INVALID_DATE_FORMAT.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativePayForOrderDetailDelivertBeforOrderDate() {
		PaymentForm pf = getPaymentInfo();
		pf.setDeliveryDate("2018-10-10");
		ResponseEntity<?> rt = orderController.payForOrder(1,"MQ==",pf);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.INVALID_DELIVERY_DATE.getMessage(), rt.getBody().toString());
	}

	private PaymentForm getPaymentInfo() {
		PaymentForm pf = new PaymentForm();
		pf.setDeliveryDate("2021-10-10");
		pf.setPaymentMethod("card");
		pf.setPaymentDate("2020-07-26");
		pf.setCardNumber("943987");
		return pf;
	}
	
	private User createValidUser() {
		User user = new User();
		user.setUserName("User1");
		user.setUserId(1);
		user.setPhone(9663507);
		user.setPassword("password");
		user.setIsAdmin(1);
		user.setEmail("user1@gmail.com");
		user.setAddress("India");
		return user;

	}
	
	private Order createValidOrder() {
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
	

	private ResponseEntity<Order> createValidCancelledOrder() {
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

	private List<Order> getOrderlist(){
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(createValidOrder());
		return orderList;
		
	}
}
