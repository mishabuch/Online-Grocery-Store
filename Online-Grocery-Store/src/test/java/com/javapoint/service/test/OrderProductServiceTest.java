package com.javapoint.service.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.javapoint.test.utils.TestUtils;
import com.javatpoint.model.OrderProduct;
import com.javatpoint.repository.OrderProductRepository;
import com.javatpoint.service.OrderProductService;
import org.junit.Assert;

public class OrderProductServiceTest {

	@InjectMocks
	private OrderProductService orderProductService;

	@Mock
	private OrderProductRepository orderProductRepository;

	@Before
	public void setUp() {
		orderProductService = new OrderProductService();
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testPositiveFindById() {
		OrderProduct op = orderProductService.findById(1, TestUtils.createValidUser());
		Assert.assertEquals(1, op.getId());
	}

}
