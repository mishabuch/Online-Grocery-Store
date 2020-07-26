package com.javapoint.service.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.javapoint.enums.ErrorCodeEnums;
import com.javapoint.test.utils.TestUtils;
import com.javatpoint.model.Product;
import com.javatpoint.repository.ProductRepository;
import com.javatpoint.service.ProductService;

public class ProductServiceTest {
	
	@InjectMocks
	private ProductService productService;
	
	@Mock
	private ProductRepository productRepository;
	
	@Before
	public void setUp() {
		
		productService = new ProductService();
		MockitoAnnotations.initMocks(this);
		Mockito.doReturn(TestUtils.createProductsList()).when(productRepository).findAll();
		Mockito.doReturn(TestUtils.createValidProduct()).when(productRepository).findOne(Mockito.anyInt());
		Mockito.doReturn(TestUtils.createProductsList()).when(productRepository).findByNameContaining(Mockito.anyString());
		Mockito.doNothing().when(productRepository).delete(Mockito.anyInt());
	
	}
	
	@Test
	public void testPositiveGetAllProducts() {
		List<Product> products = productService.findAllProducts();
		Assert.assertEquals(1, products.size());
	}
	
	@Test
	public void testPositiveGetAllProductsBySearch() {
		List<Product> products = productService.searchProduct("ea");
		Assert.assertEquals(1, products.size());
	}
	
	@Test
	public void testPositiveGetProduct() {
		Product product = productService.findById(1);
		Assert.assertEquals(product.getName(),"tea");
	}
	
	@Test
	public void addStockNegative() {
		try {
			Mockito.doReturn(null).when(productRepository).findOne(Mockito.anyInt());
			productService.addStock(1, 1);
		} catch (Exception e) {
			Assert.assertEquals(ErrorCodeEnums.PRODUCT_NOT_EXIST.getMessage(),e.getMessage());
		}
	}
	
	@Test
	public void decreaseStockNegativeNoProduct() {
		try {
			Mockito.doReturn(null).when(productRepository).findOne(Mockito.anyInt());
			productService.decreaseStock(1, 1);
		} catch (Exception e) {
			Assert.assertEquals(ErrorCodeEnums.PRODUCT_NOT_EXIST.getMessage(),e.getMessage());
		}
	}
	
	@Test
	public void decreaseStockNegativeLessQuantity() {
		try {
			Mockito.doReturn(createInValidProduct()).when(productRepository).findOne(Mockito.anyInt());
			productService.decreaseStock(2, 1);
		} catch (Exception e) {
			Assert.assertEquals(ErrorCodeEnums.PRODUCT_NOT_ENOUGH.getMessage(),e.getMessage());
		}
	}
	
	@Test
	public void deleteProductNegative() {
		try {
			Mockito.doReturn(null).when(productRepository).findOne(Mockito.anyInt());
			productService.delete(1);
		} catch (Exception e) {
			Assert.assertEquals(ErrorCodeEnums.PRODUCT_NOT_EXIST.getMessage(),e.getMessage());
		}
	}
	
	
	private Product createInValidProduct() {
		Product product = new Product();
		product.setMerchant("unilever");
		product.setName("tea2");
		product.setExpiryDate("2020-10-01");
		product.setPrice(100);
		product.setProductId(2);
		product.setQuantityInInventory(-100);
		return product;
	}
	



}
