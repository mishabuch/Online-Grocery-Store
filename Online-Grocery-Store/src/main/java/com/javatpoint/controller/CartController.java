package com.javatpoint.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javapoint.constants.Constants;
import com.javapoint.enums.ErrorCodeEnums;
import com.javapoint.exception.CustomException;
import com.javatpoint.model.AddProductToCartInput;
import com.javatpoint.model.OrderProduct;
import com.javatpoint.model.Product;
import com.javatpoint.model.User;
import com.javatpoint.service.CartService;
import com.javatpoint.service.OrderProductService;
import com.javatpoint.service.ProductService;
import com.javatpoint.service.UserService;

@RestController
public class CartController {
	@Autowired
	CartService cartService;

	@Autowired
	UserService userService;

	@Autowired
	ProductService productService;

	@Autowired
	OrderProductService orderProductService;

	// Adding a product to cart
	// Input Product id and quantity
	@PostMapping(Constants.addToCart)
	public ResponseEntity<Object> addToCart(@RequestBody AddProductToCartInput iform,
			@RequestHeader(Constants.authTokenheader) String token)   {
		if(token==null || token.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage());
		}
		User user = getUserId(token);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage());
		}
		// Get the product from product service
		Product product = productService.findById(iform.getProductId());
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.dateFormat, Locale.ENGLISH); 
		Date d = new Date();
		Date expiry;
		try {
			expiry = sdf.parse(product.getExpiryDate());
		} catch (ParseException e1) {
			return ResponseEntity.badRequest().body(ErrorCodeEnums.INVALID_DATE_FORMAT.getMessage());
		}
		if(expiry.before(d)) {
			return ResponseEntity.badRequest().body(ErrorCodeEnums.CANNOT_ADD_EXPIRED_PRODUCT_TO_CART.getMessage());
		}
		try {
			// Create an order to product entity. The order Id would be null at this point.
			// It would be set after cart checkout
			OrderProduct op = new OrderProduct(product, iform.getQuantity());
			try {
				// Add this entity to cart of the given user
				cartService.editCart(Collections.singleton(op), user);
			} catch (Exception e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
			// Return the cart details
			return ResponseEntity.ok(cartService.getCart(user));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ErrorCodeEnums.COULD_NOT_ADD_PRODUCT_TO_CART.getMessage());
		}

	}

	// Edit a product in the cart. You can only change the quantity of the product
	// in the cart
	// Pass -ve quantity to decrease the quantity in cart. along with productId
	@PutMapping(Constants.cart + Constants.forwardSlash + "{productId}")
	public ResponseEntity<?> updateProductInCart(@PathVariable("productId") int productId,
			@RequestParam Integer quantity, @RequestHeader(Constants.authTokenheader) String token) {
		if(token==null || token.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage());
		}
		User user = getUserId(token);
		// Update the cart and return cart details
		if (user != null) {
			orderProductService.update(productId, quantity, user);
			return ResponseEntity.status(HttpStatus.OK).body(cartService.getCart(user));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage());

	}

	// Get Cart Details
	@GetMapping(Constants.cart)
	public ResponseEntity<?> getCartDetails(@RequestHeader(Constants.authTokenheader) String token) {
		if(token==null || token.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage());
		}
		User user = getUserId(token);
		if (user != null) {
			return ResponseEntity.status(HttpStatus.OK).body(cartService.getCart(user));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage());
	}

	// Deleting a product from cart. This will remove the product entirely from the
	// cart
	@DeleteMapping(Constants.cart + Constants.forwardSlash + "{productId}")
	public ResponseEntity<?> removeProductFromCart(@PathVariable("productId") int productId,
			@RequestHeader(Constants.authTokenheader) String token) {
		if(token==null || token.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage());
		}
		User user = getUserId(token);
		if (user != null) {
			cartService.delete(productId, user);
			return ResponseEntity.status(HttpStatus.OK).body(cartService.getCart(user));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage());

	}

	// Checkout from the cart. This will create an order for the user with the list
	// of items in the cart
	// Note that once an order is created, user can not change the product/quantity
	// of the order.
	@PostMapping(Constants.checkout)
	public ResponseEntity<?> checkout(@RequestHeader(Constants.authTokenheader) String token) {
		if(token==null || token.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage());
		}
		User user = getUserId(token);
		try {
			if (user != null)
				cartService.checkout(user);
			else
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage());
		} catch (CustomException e) {
			// You cannot create an order for an empty cart
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(Constants.orderCreated + " with id "
				+ user.getCart().getProductsInOrder().stream().findFirst().get().getOrder().getOrderId());
	}

	public User getUserId(String token) {
		byte[] byteArray = Base64.getDecoder().decode(token);
		String decodedString = new String(byteArray);
		return userService.getUserById(Integer.parseInt(decodedString));

	}

	
}
