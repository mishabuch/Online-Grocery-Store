package com.javatpoint.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javapoint.constants.Constants;
import com.javapoint.enums.ErrorCodeEnums;
import com.javatpoint.model.Product;
import com.javatpoint.model.User;
import com.javatpoint.service.ProductService;
import com.javatpoint.service.UserService;

@RestController
public class ProductController {

	@Autowired
	ProductService productService;

	@Autowired
	UserService userService;

	// Create a Product. Only an admin can perform this operation
	@PostMapping(Constants.productEndPoint)
	public ResponseEntity<Object> createProduct(@Valid @RequestBody Product product,
			@RequestHeader(Constants.authTokenheader) String token) {
		if (token == null || token.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage());
		}
		User user = getUserId(token);
		// Check if user is admin
		if (user == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage());
		}
		if (user.getIsAdmin() == 0) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorCodeEnums.UNAUTHORIZED_ACCESS.getMessage());
		}
		Product productIdExists = productService.findById(product.getProductId());
		// If product with that id already exists
		if (productIdExists != null) {
			return ResponseEntity.badRequest().body(ErrorCodeEnums.PRODUCT_ALREADY_EXISTS.getMessage());
		}
		// Check if the expiry date of the product is befor the order date.
		// in this case, we do not allow the user to add it to cart
		try {
			if(!product.getExpiryDate().isEmpty()) {
				SimpleDateFormat sdf = new SimpleDateFormat(Constants.dateFormat, Locale.ENGLISH); 
				Date d = new Date();
				Date expiry = sdf.parse(product.getExpiryDate());
				if(expiry.before(d)) {
					return ResponseEntity.badRequest().body(ErrorCodeEnums.CANNOT_ADD_EXPIRED_PRODUCT_TO_INVENTORY.getMessage());
				}
			}
			
		}catch(ParseException e) {
			return ResponseEntity.badRequest().body(ErrorCodeEnums.INVALID_DATE_FORMAT.getMessage());
		}
		// Save the product
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveProduct(product));
	}

	// Get the product details by product ID
	@GetMapping(Constants.productEndPoint + Constants.forwardSlash + "{productId}")
	public ResponseEntity<?> getProductDetails(@PathVariable("productId") int productId) {

		Product product = productService.findById(productId);
		// Check if project exists
		if (product == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorCodeEnums.PRODUCT_NOT_EXIST.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(product);
	}

	// Search a product using a query string. Returns all products containing the
	// querystring
	@GetMapping(Constants.productEndPoint + Constants.forwardSlash + Constants.search)
	public ResponseEntity<?> searchProducts(@RequestParam(name = "name", defaultValue = "") String searchString) {
		List<Product> products = productService.searchProduct(searchString);
		// Check for no results
		if (products.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorCodeEnums.NO_RESULTS_FOUND.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(products);
	}

	// Get list of all products
	@GetMapping(Constants.productsEndPoint)
	public ResponseEntity<List<Product>> getAllProducts() {
		return ResponseEntity.status(HttpStatus.OK).body(productService.findAllProducts());
	}

	// Delete a product from inventory. Only an Admin user can perform this
	// operation
	@DeleteMapping(Constants.productEndPoint + Constants.delete + Constants.forwardSlash + "{productId}")
	public ResponseEntity<String> delete(@PathVariable("productId") int productId,
			@RequestHeader(Constants.authTokenheader) String token) {

		if (token == null || token.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage());
		}
		User user = getUserId(token);
		// Check if user is admin
		if (user == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage());
		}
		if (user.getIsAdmin() == 1) {
			try {
				// Delete the product.
				productService.delete(productId);
				return ResponseEntity.noContent().build();
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(ErrorCodeEnums.COULD_NOT_DELETE_PRODUCT.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorCodeEnums.UNAUTHORIZED_ACCESS.getMessage());

	}

	public User getUserId(String token) {
		byte[] byteArray = Base64.getDecoder().decode(token);
		String decodedString = new String(byteArray);
		return userService.getUserById(Integer.parseInt(decodedString));
	}

}
