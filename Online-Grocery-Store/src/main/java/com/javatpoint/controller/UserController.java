package com.javatpoint.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.javapoint.constants.Constants;
import com.javapoint.enums.ErrorCodeEnums;
import com.javatpoint.model.Cart;
import com.javatpoint.model.LoginForm;
import com.javatpoint.model.User;
import com.javatpoint.service.CartService;
import com.javatpoint.service.UserService;
/**
 * 
 * Author : Amisha Buch Joshipara 
 * Date : 23/7/2020 
 * 
 * */
@RestController
public class UserController {
	@Autowired
	UserService userService;

	@Autowired
	CartService cartService;

	// Create a User - Input
	// Username, password, email, phone, address
	@PostMapping(Constants.userEndPoint)
	public ResponseEntity<String> saveUser(@RequestBody User user) throws URISyntaxException {
		
		// Validation of User input fields
		if((user.getEmail()==null || user.getEmail().isEmpty())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(ErrorCodeEnums.EMAIL_MISSING.getMessage());
		}
		if((user.getPassword()==null || user.getPassword().isEmpty())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(ErrorCodeEnums.PASSWORD_MISSING.getMessage());
		}
		if((user.getAddress()==null || user.getAddress().isEmpty())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(ErrorCodeEnums.ADDRESS_MISSING.getMessage());
		}
		if((user.getUserName()==null || user.getUserName().isEmpty())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(ErrorCodeEnums.USERNAME_MISSING.getMessage());
		}
		
		// Save the user if all valid inputs given. When a user is saved, a "Cart" is also created for the user by default.
		// The cart will be initially empty.
		
		try {
			userService.saveOrUpdate(user);
			Cart cart = new Cart(user);
			cartService.createCart(cart);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		URI uri = new URI(Constants.localHostURL + Constants.userEndPoint + Constants.forwardSlash + user.getUserId());
		
		// Successful response gives the uri to the User Resource in response
		return ResponseEntity.status(HttpStatus.CREATED).body(uri.toString());
	}
	
	// Login User - Input - username , password
	@PostMapping(Constants.userEndPoint+Constants.login)
	public ResponseEntity<String> loginUser(@RequestBody LoginForm loginUser) {
		// First check if entered fields are not empty
		if (validateLoginInput(loginUser.getUsername(), loginUser.getPassword())) {
			User fromDb = userService.getUserByName(loginUser.getUsername());
			
			// If user doesnot exist, return error
			if (fromDb == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage());
			}
			//If passwords mismatch, return error or else login
			if (loginUser.getPassword().equals(fromDb.getPassword())) {
				HttpHeaders headers = getAuthTokenHeader(fromDb);
				return new ResponseEntity<>(Constants.loginSuccesful + fromDb.getUserName(), headers, HttpStatus.OK);
			} else
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(ErrorCodeEnums.PASSWORD_MISMATCH.getMessage());
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(ErrorCodeEnums.USERNAME_PASSWORD_MISSING.getMessage());
		}

	}

	// Get all Users in the System. This can only be performed by an Admin user, hence, an Auth-Token of
	// the user is passed to verify access to resource
	@GetMapping(Constants.usersEndPoint)
	public ResponseEntity<?> getAllUsers(@RequestHeader(Constants.authTokenheader) String token) {
		User user = getUserId(token);
		if(user.getIsAdmin()!=1) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorCodeEnums.UNAUTHORIZED_ACCESS.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
	}

	// Get User Details 
	@GetMapping(Constants.userEndPoint+Constants.forwardSlash+"{id}")
	public ResponseEntity<?> getUser(@PathVariable("id") int id, @RequestHeader(Constants.authTokenheader) String token) {
		
		// First we check if the token of the user passed matched the id in the URi, if he has access to this resource
		User user = getUserId(token);
		if(id!=user.getUserId()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorCodeEnums.UNAUTHORIZED_ACCESS.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
	}

	// Delete a User from the system
	// Delete can only be performed by an admin, hence token is passed to determine access
	@DeleteMapping(Constants.userEndPoint+Constants.forwardSlash+"{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") int id,  @RequestHeader(Constants.authTokenheader) String token) {
		if(token==null || token.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage());
		}
		User user = getUserId(token);
		if(user==null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage());
		}
		if(user.getIsAdmin()!=1) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorCodeEnums.UNAUTHORIZED_ACCESS.getMessage());
		}
		try {
			userService.delete(id);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Constants.userDeletedSuccessfully);
	}
	
	// UTILITY METHODS
	private HttpHeaders getAuthTokenHeader(User fromDb) {
		String token = Base64.getEncoder().encodeToString(Integer.toString(fromDb.getUserId()).getBytes());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Auth-Token", token);
		return headers;
	}

	private boolean validateLoginInput(String userName, String password) {
		return (userName != null && password != null) && (!userName.isEmpty() && !password.isEmpty());
	}
	
	public User getUserId(String token) {
		byte[] byteArray = Base64.getDecoder().decode(token);
		String decodedString = new String(byteArray);
		return userService.getUserById(Integer.parseInt(decodedString));
	}
}
