package com.javatpoint.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javapoint.enums.ErrorCodeEnums;
import com.javapoint.exception.CustomException;
import com.javatpoint.model.Product;
import com.javatpoint.repository.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	ProductRepository productRepository;
	
	// Find a product by its ID
	public Product findById(int productId) {
		return productRepository.findOne(productId);
	}

	//Find all products
	public List<Product> findAllProducts(){
		List<Product> products = new ArrayList<>();
		productRepository.findAll().forEach(product -> products.add(product));
		return products;
	}

	// Add product stock - increase quantity in inventory
	@Transactional
	public void addStock(int productId, int quantity) {
		Product product = findById(productId);
		if (product == null)
			throw new CustomException(ErrorCodeEnums.PRODUCT_NOT_EXIST);
		product.setQuantityInInventory(product.getQuantityInInventory() + quantity);
		try {
			productRepository.save(product);
		} catch (Exception e) {
			throw new CustomException(ErrorCodeEnums.COULD_NOT_ADD_BACK_STOCK);
		}
	}
	
	// Decrease stock - decrease product quantity in inventory
	@Transactional
	public void decreaseStock(int productId, int quantity){
		Product product = findById(productId);
		if (product == null)
			throw new CustomException(ErrorCodeEnums.PRODUCT_NOT_EXIST);

		int quantityInStock = product.getQuantityInInventory() - quantity;
		if (quantityInStock <= 0)
			throw new CustomException(ErrorCodeEnums.PRODUCT_NOT_ENOUGH);

		product.setQuantityInInventory(quantityInStock);
		try {
			productRepository.save(product);
		} catch (Exception e) {
			throw new CustomException(ErrorCodeEnums.COULD_NOT_SAVE_PRODUCT);
		}
	}

	// Save the product
	@Transactional
	public Product saveProduct(Product productInfo) {
		return productRepository.save(productInfo);
	}
	
	//Search a product in inventory by a string
	public List<Product> searchProduct(String searchString){
		return productRepository.findByNameContaining(searchString);
	}

	// delete product from the inventory
	@Transactional
	public void delete(int productId){
		Product product = findById(productId);
        if (product == null) throw new CustomException(ErrorCodeEnums.PRODUCT_NOT_EXIST);
        try {
			productRepository.delete(product);
		} catch (Exception e) {
			throw new CustomException(ErrorCodeEnums.COULD_NOT_DELETE_PRODUCT);
		}
	}

}