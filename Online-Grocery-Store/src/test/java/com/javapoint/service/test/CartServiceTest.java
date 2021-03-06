package com.javapoint.service.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.javapoint.enums.ErrorCodeEnums;
import com.javatpoint.model.Cart;
import com.javatpoint.model.OrderProduct;
import com.javatpoint.model.User;
import com.javatpoint.repository.CartRepository;
import com.javatpoint.repository.OrderProductRepository;
import com.javatpoint.repository.OrderRepository;
import com.javatpoint.repository.UserRepository;
import com.javatpoint.service.CartService;
import com.javatpoint.service.ProductService;
import com.javatpoint.service.UserService;

public class CartServiceTest {
	
	@InjectMocks
	private CartService cartService;

	@Mock
	private ProductService productService;

	@Mock
	private OrderRepository orderRepository;
	
	@Mock
	private UserRepository userRepository;

	@Mock
	private OrderProductRepository orderProductRepository;

	@Mock
	private CartRepository cartRepository;

	@Mock
	private UserService userService;


	@Before
	public void setUp() {

		cartService = new CartService();
		MockitoAnnotations.initMocks(this);
}

	@Test
	public void testPositiveGetOrder() {
		Cart cart = cartService.getCart(createValidUser());
		Assert.assertEquals(cart.getCartId(), 1);
	}
	
	@Test
	public void testPositiveCheckoutOrder() {
		try {
			cartService.checkout(createInValidUser());
		} catch (Exception e) {
			Assert.assertEquals(ErrorCodeEnums.CART_IS_EMPTY.getMessage(), e.getMessage());
		}
		
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
		
		Cart cart = new Cart();
		cart.setCartId(1);
		Set<OrderProduct> productsInOrder = new HashSet<>();
		OrderProduct op = new OrderProduct();
		op.setCount(1);
		op.setId(1);
		op.setProductId(1);
		op.setProductName("tea");
		op.setProductPrice(100);
		productsInOrder.add(op);
		cart.setProductsInOrder(productsInOrder);
		
		user.setCart(cart);
		return user;

	}
	
	private User createInValidUser() {
		User user = new User();
		user.setUserName("User1");
		user.setUserId(1);
		user.setPhone(9663507);
		user.setPassword("password");
		user.setIsAdmin(1);
		user.setEmail("user1@gmail.com");
		user.setAddress("India");
		
		Cart cart = new Cart();
		cart.setCartId(1);
		Set<OrderProduct> productsInOrder = new HashSet<>();
		OrderProduct op = new OrderProduct();
		op.setCount(1);
		op.setId(1);
		op.setProductId(1);
		op.setProductName("tea");
		op.setProductPrice(100);
		productsInOrder.add(op);
		//cart.setProductsInOrder(productsInOrder);
		
		user.setCart(cart);
		return user;

	}
}
