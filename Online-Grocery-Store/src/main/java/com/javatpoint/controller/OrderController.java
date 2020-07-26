package com.javatpoint.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.javapoint.constants.Constants;
import com.javapoint.enums.ErrorCodeEnums;
import com.javapoint.enums.StatusEnum;
import com.javatpoint.model.Order;
import com.javatpoint.model.PaymentForm;
import com.javatpoint.model.User;
import com.javatpoint.service.OrderService;
import com.javatpoint.service.UserService;

@RestController
public class OrderController {
	@Autowired
	OrderService orderService;

	@Autowired
	UserService userService;

	// Get list of all orders for a user.
	@GetMapping(Constants.ordersEndPoint)
	public ResponseEntity<?> orderList(@RequestHeader(Constants.authTokenheader) String token) {
		if (token == null || token.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage());
		}
		User user = getUserId(token);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(orderService.findByUserEmail(user.getEmail()));
	}

	// Get details of an order for a user. A user can view only one's own order
	@GetMapping(Constants.orderEndPoint + Constants.forwardSlash + "{orderId}")
	public ResponseEntity<?> getOrderDetails(@PathVariable("orderId") int orderId,
			@RequestHeader(Constants.authTokenheader) String token) {
		if (token == null || token.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage());
		}
		User user = getUserId(token);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage());
		}
		Order order = orderService.findByOrderId(orderId);
		// If order doesnot exist, retun 404
		if (order == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorCodeEnums.ORDER_NOT_FOUND.getMessage());
		}
		// Check if user has access to the order resource
		if (order.getUserId() != user.getUserId()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorCodeEnums.UNAUTHORIZED_ACCESS.getMessage());
		}

		return ResponseEntity.ok(order);
	}

	// PATCH MAPPINGS

	// These patch mappings change the status of the order

	// Pay for an existing order
	// Input order id in the path param, as well as the selected delivery date in
	// yyyymmdd format
	@PatchMapping(Constants.orderEndPoint + Constants.pay + Constants.forwardSlash + "{orderId}")
	public ResponseEntity<Object> payForOrder(@PathVariable("orderId") int orderId,
			@RequestHeader(Constants.authTokenheader) String token, @RequestBody PaymentForm paymentInfo) {
		// Get the order to be paid for
		Order order = orderService.findByOrderId(orderId);
		if (order == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorCodeEnums.ORDER_NOT_FOUND.getMessage());
		}

		// Convert the delivery date in yyyy-mm-dd format
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH); 
		try {
			Date deliveryDate = sdf.parse(paymentInfo.getDeliveryDate());
			Date orderDate = sdf.parse(order.getOrderDate());
			if(deliveryDate.before(orderDate)) {
				return ResponseEntity.badRequest().body(ErrorCodeEnums.INVALID_DELIVERY_DATE.getMessage());
			}
			sdf.parse(paymentInfo.getPaymentDate());
		} catch (ParseException e) {
			return ResponseEntity.badRequest().body(ErrorCodeEnums.INVALID_DATE_FORMAT.getMessage());
		}

		if (token == null || token.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage());
		}
		User user = getUserId(token);
		// Check if user has access to this order
		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage());
		} else if ((order.getUserId() != user.getUserId())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorCodeEnums.UNAUTHORIZED_ACCESS.getMessage());
		}

		// Change status to PLACED, as order is paid
		if (order.getStatus().equals(StatusEnum.CREATED.getMsg())) {
			order.setStatus(StatusEnum.PLACED.getMsg());
			order.setPaymentMethod(paymentInfo.getPaymentMethod());
			if (order.getPaymentMethod().equalsIgnoreCase(Constants.card)) {
				order.setCardNumber(paymentInfo.getCardNumber());
				order.setTransactionID(Math.random());
			} else {
				order.setCardNumber("");
				order.setTransactionID(0);
			}
			order.setDeliveryDate(paymentInfo.getDeliveryDate());
			order.setPaymentDate(paymentInfo.getPaymentDate());
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorCodeEnums.INVALID_STATUS.getMessage());
		}
		return ResponseEntity.ok(orderService.saveUpdate(order));
	}

	// Cancel a placed order.
	// Order cannot be cancelled unless placed. (i.e paid for)
	@PatchMapping(Constants.orderEndPoint + Constants.cancel + Constants.forwardSlash + "{orderId}")
	public ResponseEntity<?> cancelOrder(@PathVariable("orderId") int orderId,
			@RequestHeader(Constants.authTokenheader) String token) {
		// Find the order
		Order order = orderService.findByOrderId(orderId);
		if (order == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorCodeEnums.ORDER_NOT_FOUND.getMessage());
		}
		if (token == null || token.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage());
		}
		User user = getUserId(token);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage());
		} else if ((order.getUserId() != user.getUserId())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorCodeEnums.UNAUTHORIZED_ACCESS.getMessage());
		}
		// Cancel the order. This function also restocks the item
		return orderService.cancelOrder(orderId);
	}

	public User getUserId(String token) {
		byte[] byteArray = Base64.getDecoder().decode(token);
		String decodedString = new String(byteArray);
		return userService.getUserById(Integer.parseInt(decodedString));
	}

}
