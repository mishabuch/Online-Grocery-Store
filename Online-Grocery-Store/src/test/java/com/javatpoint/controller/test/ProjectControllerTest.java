package com.javatpoint.controller.test;

import java.net.URISyntaxException;
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
import com.javapoint.test.utils.TestUtils;
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
		Mockito.doReturn(TestUtils.createValidProduct()).when(productService).saveProduct(Mockito.anyObject());
		Mockito.doReturn(TestUtils.createValidProduct()).when(productService).findById(Mockito.anyInt());
		Mockito.doReturn(TestUtils.createProductsList()).when(productService).findAllProducts();
		Mockito.doReturn(TestUtils.createProductsList()).when(productService).searchProduct(Mockito.anyString());
		Mockito.doNothing().when(productService).delete(Mockito.anyInt());
		Mockito.doReturn(TestUtils.createValidUser()).when(userService).getUserById(Mockito.anyInt());
	}
	
	@Test
	public void testPositiveCreateProduct() throws URISyntaxException {
		Product product = TestUtils.createValidProduct();
		Mockito.doReturn(null).when(productService).findById(Mockito.anyInt());
		ResponseEntity<?> rt = productController.createProduct(product, "MQ==");
		Assert.assertEquals(HttpStatus.CREATED, rt.getStatusCode());
	}
	
	@Test
	public void testNegativeCreateProductTokenNull() throws URISyntaxException {
		Product product = TestUtils.createValidProduct();
		Mockito.doReturn(null).when(productService).findById(Mockito.anyInt());
		ResponseEntity<?> rt = productController.createProduct(product, null);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeCreateProductTokenEmpty() throws URISyntaxException {
		Product product = TestUtils.createValidProduct();
		Mockito.doReturn(null).when(productService).findById(Mockito.anyInt());
		ResponseEntity<?> rt = productController.createProduct(product, "");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
	}
	
	
	@Test
	public void testNegativeCreateProductUserDOESNOTEXIST() throws URISyntaxException {
		Product product = TestUtils.createValidProduct();
		Mockito.doReturn(null).when(productService).findById(Mockito.anyInt());
		Mockito.doReturn(null).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = productController.createProduct(product, "MQ==");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeCreateProductUserNotAdmin() throws URISyntaxException {
		Product product = TestUtils.createValidProduct();
		User user = TestUtils.createValidUser();
		user.setIsAdmin(0);
		Mockito.doReturn(null).when(productService).findById(Mockito.anyInt());
		Mockito.doReturn(user).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = productController.createProduct(product, "MQ==");
		Assert.assertEquals(HttpStatus.UNAUTHORIZED, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.UNAUTHORIZED_ACCESS.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeCreateProductAlreadyExist() throws URISyntaxException {
		Product product = TestUtils.createValidProduct();
		ResponseEntity<?> rt = productController.createProduct(product, "MQ==");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.PRODUCT_ALREADY_EXISTS.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeCreateProductInvalidExpiry() throws URISyntaxException {
		Product product = TestUtils.createValidProduct();
		product.setExpiryDate("81982shj");
		Mockito.doReturn(null).when(productService).findById(Mockito.anyInt());
		ResponseEntity<?> rt = productController.createProduct(product, "MQ==");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.INVALID_DATE_FORMAT.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeCreateProductInvalidExpiryDate() throws URISyntaxException {
		Product product = TestUtils.createValidProduct();
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
		Mockito.doReturn(TestUtils.emptyProductList()).when(productService).searchProduct(Mockito.anyString());
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
		
		User user = TestUtils.createValidUser();
		user.setIsAdmin(0);
		Mockito.doReturn(user).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = productController.delete(1, "MQ==");
		Assert.assertEquals(HttpStatus.UNAUTHORIZED, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.UNAUTHORIZED_ACCESS.getMessage(), rt.getBody().toString());
	}
	
	
	

}
