package com.javatpoint.controller.test;

import java.net.URISyntaxException;
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
import com.javatpoint.controller.ProductController;
import com.javatpoint.model.Product;
import com.javatpoint.model.User;
import com.javatpoint.service.ProductService;
import com.javatpoint.service.UserService;

public class ProjectControllerTest {
	
	@InjectMocks
	private ProductController productController;
	
	@Mock
	private UserService userService;
	
	@Mock
	private ProductService productService;

	@Before
	public void setUp() {
		
		productController = new ProductController();
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(userService).saveOrUpdate(Mockito.anyObject());
		Mockito.doReturn(createValidProduct()).when(productService).saveProduct(Mockito.anyObject());
		Mockito.doReturn(createValidProduct()).when(productService).findById(Mockito.anyInt());
		Mockito.doReturn(createProductsList()).when(productService).findAllProducts();
		Mockito.doReturn(createProductsList()).when(productService).searchProduct(Mockito.anyString());
		Mockito.doNothing().when(productService).delete(Mockito.anyInt());
		Mockito.doReturn(createValidUser()).when(userService).getUserById(Mockito.anyInt());
	}
	
	@Test
	public void testPositiveCreateProduct() throws URISyntaxException {
		Product product = createValidProduct();
		Mockito.doReturn(null).when(productService).findById(Mockito.anyInt());
		ResponseEntity<?> rt = productController.createProduct(product, "MQ==");
		Assert.assertEquals(HttpStatus.CREATED, rt.getStatusCode());
	}
	
	@Test
	public void testNegativeCreateProductTokenNull() throws URISyntaxException {
		Product product = createValidProduct();
		Mockito.doReturn(null).when(productService).findById(Mockito.anyInt());
		ResponseEntity<?> rt = productController.createProduct(product, null);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeCreateProductTokenEmpty() throws URISyntaxException {
		Product product = createValidProduct();
		Mockito.doReturn(null).when(productService).findById(Mockito.anyInt());
		ResponseEntity<?> rt = productController.createProduct(product, "");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
	}
	
	
	@Test
	public void testNegativeCreateProductUserDOESNOTEXIST() throws URISyntaxException {
		Product product = createValidProduct();
		Mockito.doReturn(null).when(productService).findById(Mockito.anyInt());
		Mockito.doReturn(null).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = productController.createProduct(product, "MQ==");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeCreateProductUserNotAdmin() throws URISyntaxException {
		Product product = createValidProduct();
		User user = createValidUser();
		user.setIsAdmin(0);
		Mockito.doReturn(null).when(productService).findById(Mockito.anyInt());
		Mockito.doReturn(user).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = productController.createProduct(product, "MQ==");
		Assert.assertEquals(HttpStatus.UNAUTHORIZED, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.UNAUTHORIZED_ACCESS.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeCreateProductAlreadyExist() throws URISyntaxException {
		Product product = createValidProduct();
		ResponseEntity<?> rt = productController.createProduct(product, "MQ==");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.PRODUCT_ALREADY_EXISTS.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeCreateProductInvalidExpiry() throws URISyntaxException {
		Product product = createValidProduct();
		product.setExpiryDate("81982shj");
		Mockito.doReturn(null).when(productService).findById(Mockito.anyInt());
		ResponseEntity<?> rt = productController.createProduct(product, "MQ==");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.INVALID_DATE_FORMAT.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeCreateProductInvalidExpiryDate() throws URISyntaxException {
		Product product = createValidProduct();
		product.setExpiryDate("2019-10-10");
		Mockito.doReturn(null).when(productService).findById(Mockito.anyInt());
		ResponseEntity<?> rt = productController.createProduct(product, "MQ==");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.CANNOT_ADD_EXPIRED_PRODUCT_TO_INVENTORY.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testPositiveGetProductDetails() throws URISyntaxException {
		ResponseEntity<?> rt = productController.getProductDetails(1);
		Assert.assertEquals(HttpStatus.OK, rt.getStatusCode());
	}
	
	
	@Test
	public void testNegativeGetProductDetails() throws URISyntaxException {
		Mockito.doReturn(null).when(productService).findById(Mockito.anyInt());
		ResponseEntity<?> rt = productController.getProductDetails(1);
		Assert.assertEquals(HttpStatus.NOT_FOUND, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.PRODUCT_NOT_EXIST.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testPositiveSearchProduct() throws URISyntaxException {
		ResponseEntity<?> rt = productController.searchProducts("ea");
		Assert.assertEquals(HttpStatus.OK, rt.getStatusCode());
	}
	
	@Test
	public void testSearchProductNegative() throws URISyntaxException {
		Mockito.doReturn(emptyProductList()).when(productService).searchProduct(Mockito.anyString());
		ResponseEntity<?> rt = productController.searchProducts("ea");
		Assert.assertEquals(HttpStatus.NOT_FOUND, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.NO_RESULTS_FOUND.getMessage(), rt.getBody().toString());
	}
	
	
	@Test
	public void testPositiveGetAllProducts() throws URISyntaxException {
		ResponseEntity<?> rt = productController.getAllProducts();
		Assert.assertEquals(HttpStatus.OK, rt.getStatusCode());
	}
	
	@Test
	public void testPositiveDeleteProduct() throws URISyntaxException {
		ResponseEntity<?> rt = productController.delete(1, "MQ==");
		Assert.assertEquals(HttpStatus.NO_CONTENT, rt.getStatusCode());
	}
	
	@Test
	public void testNegativeDeleteProductTokenEmpty() throws URISyntaxException {
		ResponseEntity<?> rt = productController.delete(1, "");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeDeleteProductTokenNull() throws URISyntaxException {
		ResponseEntity<?> rt = productController.delete(1, null);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeDeleteProductUserDOESNOTEXIST() throws URISyntaxException {
		Mockito.doReturn(null).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = productController.delete(1, "MQ==");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeDeleteProductUserNotAdmin() throws URISyntaxException {
		
		User user = createValidUser();
		user.setIsAdmin(0);
		Mockito.doReturn(user).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = productController.delete(1, "MQ==");
		Assert.assertEquals(HttpStatus.UNAUTHORIZED, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.UNAUTHORIZED_ACCESS.getMessage(), rt.getBody().toString());
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
	
	private Object createProductsList() {
		List<Product> products = new ArrayList<>();
		products.add(createValidProduct());
		return products;
	}
	
	private Object emptyProductList() {
		List<Product> products = new ArrayList<>();
		return products;
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
	

}
