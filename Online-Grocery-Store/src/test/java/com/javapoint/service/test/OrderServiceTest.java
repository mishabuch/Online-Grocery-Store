package com.javapoint.service.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.javapoint.enums.ErrorCodeEnums;
import com.javapoint.enums.StatusEnum;
import com.javapoint.test.utils.TestUtils;
import com.javatpoint.model.Order;
import com.javatpoint.repository.OrderRepository;
import com.javatpoint.service.OrderService;
import com.javatpoint.service.ProductService;

public class OrderServiceTest {

	@InjectMocks
	private OrderService orderService;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private ProductService productService;

	@Before
	public void setUp() {

		orderService = new OrderService();
		MockitoAnnotations.initMocks(this);
		Mockito.doReturn(createValidOrder()).when(orderRepository).findOne(Mockito.anyInt());
		Mockito.doReturn(createOrdersList()).when(orderRepository).findAllByStatusAndEmail(Mockito.anyString(),
				Mockito.anyString());
		Mockito.doReturn(createOrdersList()).when(orderRepository).findAllByEmail(Mockito.anyString());
		Mockito.doReturn(TestUtils.createValidProduct()).when(productService).findById(Mockito.anyInt());
		Mockito.doNothing().when(productService).addStock(Mockito.anyInt(), Mockito.anyInt());
	}

	@Test
	public void testPositiveGetOrder() {
		Order order = orderService.findByOrderId(1);
		Assert.assertEquals(order.getTotalValue(), 100,0);
	}

	@Test
	public void testPositiveGetOrderByStatusEmail() {
		List<Order> order = orderService.findByOrderStatusAndEmail(StatusEnum.PLACED.getMsg(), "abc@gamil.com");
		Assert.assertEquals(order.size(), 1);
	}

	@Test
	public void testPositiveGetOrderByNameEmail() {
		List<Order> order = orderService.findByUserEmail("abc@gamil.com");
		Assert.assertEquals(order.size(), 1);
	}
	
	@Test
	public void testNegativeOrderNotPlaced() {
		Order order = createValidOrder();
		order.setStatus(StatusEnum.CREATED.getMsg());
		Mockito.doReturn(createValidOrder()).when(orderRepository).findOne(Mockito.anyInt());
		 try {
			orderService.cancelOrder(1);
		} catch (Exception e) {
			Assert.assertEquals(ErrorCodeEnums.ORDER_NOT_PAID_FOR, e.getMessage());
		}
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
		order.setStatus(StatusEnum.PLACED.getMsg());
		order.setTotalValue(100);
		order.setTransactionID(11133);
		order.setUserId(1);
		return order;
	}


	private Object createOrdersList() {
		List<Order> orders = new ArrayList<>();
		orders.add(createValidOrder());
		return orders;
	}

}
