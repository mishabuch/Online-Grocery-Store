package com.javatpoint.controller.test;

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
import com.javapoint.test.utils.TestUtils;
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
		Mockito.doReturn(TestUtils.createValidOrder()).when(orderService).saveUpdate(Mockito.anyObject());
		Mockito.doReturn(TestUtils.createValidCancelledOrder()).when(orderService).cancelOrder(Mockito.anyInt());
		Mockito.doReturn(TestUtils.getOrderlist()).when(orderService).findByUserEmail(Mockito.anyString());
		Mockito.doReturn(TestUtils.createValidOrder()).when(orderService).findByOrderId(Mockito.anyInt());
		Mockito.doReturn(TestUtils.createValidUser()).when(userService).getUserById(Mockito.anyInt());
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
		User user = TestUtils.createValidUser();
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
		User user = TestUtils.createValidUser();
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
		PaymentForm pf = TestUtils.getPaymentInfo();
		ResponseEntity<?> rt = orderController.payForOrder(1, "MQ==", pf);
		Assert.assertEquals(HttpStatus.OK, rt.getStatusCode());
	}
	
	@Test
	public void testNegativePayForOrderInvalidOrderStatus() {
		PaymentForm pf = TestUtils.getPaymentInfo();
		Order order = TestUtils.createValidOrder();
		order.setStatus(StatusEnum.PLACED.getMsg());
		Mockito.doReturn(order).when(orderService).findByOrderId(Mockito.anyInt());
		ResponseEntity<?> rt = orderController.payForOrder(1, "MQ==", pf);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.INVALID_STATUS.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativePayForOrderUserMismatch() {
		PaymentForm pf = TestUtils.getPaymentInfo();
		User user = TestUtils.createValidUser();
		user.setUserId(2);
		Mockito.doReturn(user).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = orderController.payForOrder(1, "MQ==", pf);
		Assert.assertEquals(HttpStatus.UNAUTHORIZED, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.UNAUTHORIZED_ACCESS.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativePayForOrderUserNull() {
		PaymentForm pf = TestUtils.getPaymentInfo();
		Mockito.doReturn(null).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = orderController.payForOrder(1, "MQ==", pf);
		Assert.assertEquals(HttpStatus.NOT_FOUND, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativePayForOrderTokenNull() {
		PaymentForm pf = TestUtils.getPaymentInfo();
		ResponseEntity<?> rt = orderController.payForOrder(1, null, pf);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
	}

	@Test
	public void testNegativePayForOrderTokenEmpty() {
		PaymentForm pf = TestUtils.getPaymentInfo();
		ResponseEntity<?> rt = orderController.payForOrder(1, "", pf);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativePayForOrderDetailOrderNotExist() {
		PaymentForm pf = TestUtils.getPaymentInfo();
		Mockito.doReturn(null).when(orderService).findByOrderId(Mockito.anyInt());
		ResponseEntity<?> rt = orderController.payForOrder(1,"MQ==",pf);
		Assert.assertEquals(HttpStatus.NOT_FOUND, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.ORDER_NOT_FOUND.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativePayForOrderDetailDelivertDateInvalid() {
		PaymentForm pf = TestUtils.getPaymentInfo();
		pf.setDeliveryDate("112weewe");
		ResponseEntity<?> rt = orderController.payForOrder(1,"MQ==",pf);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.INVALID_DATE_FORMAT.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativePayForOrderDetailDelivertBeforOrderDate() {
		PaymentForm pf = TestUtils.getPaymentInfo();
		pf.setDeliveryDate("2018-10-10");
		ResponseEntity<?> rt = orderController.payForOrder(1,"MQ==",pf);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.INVALID_DELIVERY_DATE.getMessage(), rt.getBody().toString());
	}


}
