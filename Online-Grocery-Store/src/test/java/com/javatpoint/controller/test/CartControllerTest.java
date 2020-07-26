package com.javatpoint.controller.test;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

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
import com.javatpoint.controller.CartController;
import com.javatpoint.model.AddProductToCartInput;
import com.javatpoint.model.Cart;
import com.javatpoint.model.Order;
import com.javatpoint.model.OrderProduct;
import com.javatpoint.model.Product;
import com.javatpoint.model.User;
import com.javatpoint.service.CartService;
import com.javatpoint.service.OrderProductService;
import com.javatpoint.service.ProductService;
import com.javatpoint.service.UserService;

public class CartControllerTest {
	
	@InjectMocks
	private CartController cartController;
	
	@Mock
	private CartService cartService;

	@Mock
	private UserService userService;

	@Mock
	private ProductService productService;

	@Mock
	private OrderProductService orderProductService;
	
	@Before
	public void setUp() {
		
		cartController = new CartController();
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(cartService).editCart(Mockito.anyCollection(), Mockito.anyObject());
		Mockito.doNothing().when(cartService).checkout(Mockito.anyObject());
		Mockito.doNothing().when(cartService).delete(Mockito.anyInt(), Mockito.anyObject());
		Mockito.doNothing().when(orderProductService).update(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyObject());
		Mockito.doReturn(createValidCart()).when(cartService).getCart(Mockito.anyObject());
		Mockito.doReturn(createValidUser()).when(userService).getUserById(Mockito.anyInt());
		Mockito.doReturn(createValidProduct()).when(productService).findById(Mockito.anyInt());
	
	}
	
	@Test
	public void testPositiveAddToCart() throws ParseException {
		AddProductToCartInput form = getAddProductForm();
		ResponseEntity<?> rt = cartController.addToCart(form,"MQ==");
		Assert.assertEquals(HttpStatus.OK, rt.getStatusCode());
	}
	
	@Test
	public void testNegativeAddToCartTokenEmpty() throws ParseException {
		AddProductToCartInput form = getAddProductForm();
		ResponseEntity<?> rt = cartController.addToCart(form,"");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
	}
	@Test
	public void testNegativeAddToCartTokenNull() throws ParseException {
		AddProductToCartInput form = getAddProductForm();
		ResponseEntity<?> rt = cartController.addToCart(form,null);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeAddToCartUserNull() throws ParseException {
		AddProductToCartInput form = getAddProductForm();
		Mockito.doReturn(null).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = cartController.addToCart(form,"MQ==");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeAddToCartProductDateInvalid() throws ParseException {
		AddProductToCartInput form = getAddProductForm();
		Product product = createValidProduct();
		product.setExpiryDate("jdakj21");
		Mockito.doReturn(product).when(productService).findById(Mockito.anyInt());
		ResponseEntity<?> rt = cartController.addToCart(form,"MQ==");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.INVALID_DATE_FORMAT.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeAddToCartProductExpiryDateInvalid() throws ParseException {
		AddProductToCartInput form = getAddProductForm();
		Product product = createValidProduct();
		product.setExpiryDate("2018-01-01");
		Mockito.doReturn(product).when(productService).findById(Mockito.anyInt());
		ResponseEntity<?> rt = cartController.addToCart(form,"MQ==");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.CANNOT_ADD_EXPIRED_PRODUCT_TO_CART.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testPositiveUpdateProductToCart() throws ParseException {
		ResponseEntity<?> rt = cartController.updateProductInCart(1,1,"MQ==");
		Assert.assertEquals(HttpStatus.OK, rt.getStatusCode());
	}
	
	@Test
	public void testPositiveUpdateProductToCartTokenNull() throws ParseException {
		ResponseEntity<?> rt = cartController.updateProductInCart(1,1,null);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testPositiveUpdateProductToCartTokenEMpty() throws ParseException {
		ResponseEntity<?> rt = cartController.updateProductInCart(1,1,"");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testPositiveUpdateProductToCartUserNull() throws ParseException {
		Mockito.doReturn(null).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = cartController.updateProductInCart(1,1,"MQ==");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testPositiveGetCartDetails() throws ParseException {
		ResponseEntity<?> rt = cartController.getCartDetails("MQ==");
		Assert.assertEquals(HttpStatus.OK, rt.getStatusCode());
		
	}
	
	@Test
	public void testNegativeeGetCartDetailsUserDoesNotExist() throws ParseException {
		Mockito.doReturn(null).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = cartController.getCartDetails("MQ==");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage(), rt.getBody().toString());
		
	}
	
	@Test
	public void testNegativeGetCartDetailsUserTokenEmpty() throws ParseException {
		ResponseEntity<?> rt = cartController.getCartDetails("");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
		
	}
	
	@Test
	public void testNegativeGetCartDetailsUserTokenNull() throws ParseException {
		ResponseEntity<?> rt = cartController.getCartDetails(null);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
		
	}
	
	@Test
	public void testPositiveDeleteCartDetails() throws ParseException {
		ResponseEntity<?> rt = cartController.removeProductFromCart(1,"MQ==");
		Assert.assertEquals(HttpStatus.OK, rt.getStatusCode());
		
	}
	
	@Test
	public void testNegativeeDeleteProductsUserDoesNotExist() throws ParseException {
		Mockito.doReturn(null).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = cartController.removeProductFromCart(1,"MQ==");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage(), rt.getBody().toString());
		
	}
	
	@Test
	public void testNegativeDeleteProductsUserTokenEmpty() throws ParseException {
		ResponseEntity<?> rt = cartController.removeProductFromCart(1,"");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
		
	}
	
	@Test
	public void testNegativeDeleteCartDetailsUserTokenNull() throws ParseException {
		ResponseEntity<?> rt = cartController.removeProductFromCart(1,null);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
		
	}
	
	@Test
	public void testPositiveCheckOutCart() throws ParseException {
		ResponseEntity<?> rt = cartController.checkout("MQ==");
		Assert.assertEquals(HttpStatus.OK, rt.getStatusCode());
	}
	
	@Test
	public void testNegativeCheckoutDoesNotExist() throws ParseException {
		Mockito.doReturn(null).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = cartController.checkout("MQ==");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage(), rt.getBody().toString());
		
	}
	
	@Test
	public void testNegativeCheckoutUserTokenEmpty() throws ParseException {
		ResponseEntity<?> rt = cartController.checkout("");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
		
	}
	
	@Test
	public void testNegativeCheckoutCartDetailsUserTokenNull() throws ParseException {
		ResponseEntity<?> rt = cartController.checkout(null);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
		
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
		user.setCart(createValidCart());
		return user;

	}
	
	private Product createValidProduct() {
		Product product = new Product();
		product.setMerchant("unilever");
		product.setName("tea");
		product.setExpiryDate("2020-10-01");
		product.setPrice(100);
		product.setProductId(1);
		product.setQuantityInInventory(100);
		return product;
	}
	
	private Cart createValidCart() {
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
	
	private AddProductToCartInput getAddProductForm() {
		AddProductToCartInput  form = new AddProductToCartInput();
		form.setProductId(1);
		form.setQuantity(1);
		return form;
	}
}
