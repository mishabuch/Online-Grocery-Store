package com.javatpoint.controller.test;

import java.net.URISyntaxException;
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
import com.javatpoint.controller.UserController;
import com.javatpoint.model.LoginForm;
import com.javatpoint.model.User;
import com.javatpoint.service.CartService;
import com.javatpoint.service.UserService;
import org.junit.Assert; 

public class UserControllerTest {
	
	@InjectMocks
	private UserController userController;
	
	@Mock
	private UserService userService;
	
	@Mock
	private CartService cartService;

	@Before
	public void setUp() {
		
		userController = new UserController();
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(userService).saveOrUpdate(Mockito.anyObject());
		Mockito.doNothing().when(cartService).createCart(Mockito.anyObject());
		Mockito.doReturn(TestUtils.createValidUser()).when(userService).getUserByName(Mockito.anyString());
		Mockito.doReturn(TestUtils.createUsersList()).when(userService).getAllUsers();
	}

	@Test
	public void testPositiveCreateUser() throws URISyntaxException {
		User user = TestUtils.createValidUser();
		ResponseEntity<?> rt = userController.saveUser(user);
		Assert.assertEquals(HttpStatus.CREATED, rt.getStatusCode());
		Assert.assertEquals("http://localhost:8080/user/1", rt.getBody().toString());
	}
	
	@Test
	public void testNegativeCreateUserNameNull() throws URISyntaxException {
		User user = TestUtils.createValidUser();
		user.setUserName(null);
		ResponseEntity<?> rt = userController.saveUser(user);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.USERNAME_MISSING.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeCreateUserNameEmpty() throws URISyntaxException {
		User user = TestUtils.createValidUser();
		user.setUserName("");
		ResponseEntity<?> rt = userController.saveUser(user);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.USERNAME_MISSING.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeCreateUserEmailNull() throws URISyntaxException {
		User user = TestUtils.createValidUser();
		user.setEmail(null);
		ResponseEntity<?> rt = userController.saveUser(user);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.EMAIL_MISSING.getMessage(), rt.getBody().toString());
	}

	@Test
	public void testNegativeCreateUserEmailEmpty() throws URISyntaxException {
		User user = TestUtils.createValidUser();
		user.setEmail("");
		ResponseEntity<?> rt = userController.saveUser(user);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.EMAIL_MISSING.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeCreateUserPasswordEmpty() throws URISyntaxException {
		User user = TestUtils.createValidUser();
		user.setPassword("");
		ResponseEntity<?> rt = userController.saveUser(user);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.PASSWORD_MISSING.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeCreateUserPasswordNull() throws URISyntaxException {
		User user = TestUtils.createValidUser();
		user.setPassword(null);
		ResponseEntity<?> rt = userController.saveUser(user);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.PASSWORD_MISSING.getMessage(), rt.getBody().toString());
	}
	
	
	@Test
	public void testNegativeCreateUserAddressNull() throws URISyntaxException {
		User user = TestUtils.createValidUser();
		user.setAddress(null);
		ResponseEntity<?> rt = userController.saveUser(user);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.ADDRESS_MISSING.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegativeCreateUserAddressEmpty() throws URISyntaxException {
		User user = TestUtils.createValidUser();
		user.setAddress("");
		ResponseEntity<?> rt = userController.saveUser(user);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.ADDRESS_MISSING.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testpositiveLoginUser() throws URISyntaxException {
		Mockito.doReturn(TestUtils.createValidUser()).when(userService).getUserByName(Mockito.anyString());
		ResponseEntity<?> rt = userController.loginUser(TestUtils.createLoginForm());
		Assert.assertEquals(HttpStatus.OK, rt.getStatusCode());
		Assert.assertEquals("Login Successful for user User1", rt.getBody().toString());
	}
	
	@Test
	public void testnegativeLoginInvalidUser() throws URISyntaxException {
		Mockito.doReturn(null).when(userService).getUserByName(Mockito.anyString());
		ResponseEntity<?> rt = userController.loginUser(TestUtils.createLoginForm());
		Assert.assertEquals(HttpStatus.NOT_FOUND, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testnegativeLoginInvalidPassword() throws URISyntaxException {
		User us = TestUtils.createValidUser();
		us.setPassword("jjashj");
		Mockito.doReturn(us).when(userService).getUserByName(Mockito.anyString());
		ResponseEntity<?> rt = userController.loginUser(TestUtils.createLoginForm());
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.PASSWORD_MISMATCH.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testnegativeLoginInvalidPasswordNull() throws URISyntaxException {
		LoginForm lf = TestUtils.createLoginForm();
		lf.setPassword(null);
		Mockito.doReturn(TestUtils.createValidUser()).when(userService).getUserByName(Mockito.anyString());
		ResponseEntity<?> rt = userController.loginUser(lf);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.USERNAME_PASSWORD_MISSING.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testnegativeLoginInvalidUserNameNull() throws URISyntaxException {
		LoginForm lf = TestUtils.createLoginForm();
		lf.setUsername(null);
		Mockito.doReturn(TestUtils.createValidUser()).when(userService).getUserByName(Mockito.anyString());
		ResponseEntity<?> rt = userController.loginUser(lf);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.USERNAME_PASSWORD_MISSING.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testnegativeLoginInvalidPasswordEmpty() throws URISyntaxException {
		LoginForm lf = TestUtils.createLoginForm();
		lf.setPassword("");
		Mockito.doReturn(TestUtils.createValidUser()).when(userService).getUserByName(Mockito.anyString());
		ResponseEntity<?> rt = userController.loginUser(lf);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.USERNAME_PASSWORD_MISSING.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testnegativeLoginInvalidUserNameEmpty() throws URISyntaxException {
		LoginForm lf = TestUtils.createLoginForm();
		lf.setUsername("");
		Mockito.doReturn(TestUtils.createValidUser()).when(userService).getUserByName(Mockito.anyString());
		ResponseEntity<?> rt = userController.loginUser(lf);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.USERNAME_PASSWORD_MISSING.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testPositiveGetAllUsers() throws URISyntaxException {
		Mockito.doReturn(TestUtils.createValidUser()).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = userController.getAllUsers("MQ==");
		Assert.assertEquals(HttpStatus.OK, rt.getStatusCode());
	}
	
	@Test
	public void testNegativeUserNotAdmin() throws URISyntaxException {
		User user = TestUtils.createValidUser();
		user.setIsAdmin(0);
		Mockito.doReturn(user).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = userController.getAllUsers("MQ==");
		Assert.assertEquals(HttpStatus.UNAUTHORIZED, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.UNAUTHORIZED_ACCESS.getMessage(), rt.getBody().toString());
	}

	@Test
	public void testNegativeGetUserNotAdmin() throws URISyntaxException {
		User user = TestUtils.createValidUser();
		user.setIsAdmin(0);
		Mockito.doReturn(user).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = userController.getUser(2,"MQ==");
		Assert.assertEquals(HttpStatus.UNAUTHORIZED, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.UNAUTHORIZED_ACCESS.getMessage(), rt.getBody().toString());
	}

	@Test
	public void testPositiveGetUser() throws URISyntaxException {
		Mockito.doReturn(TestUtils.createValidUser()).when(userService).getUserById(1);
		ResponseEntity<?> rt = userController.getUser(1,"MQ==");
		Assert.assertEquals(HttpStatus.OK, rt.getStatusCode());
	}
	
	@Test
	public void testPositiveDeleteUser() throws URISyntaxException {
		Mockito.doReturn(TestUtils.createValidUser()).when(userService).getUserById(Mockito.anyInt());
		Mockito.doNothing().when(userService).delete(Mockito.anyInt());
		ResponseEntity<?> rt = userController.deleteUser(1,"MQ==");
		Assert.assertEquals(HttpStatus.NO_CONTENT, rt.getStatusCode());
	}
	
	@Test
	public void testNegatiiveDeleteUserIsNotAdmin() throws URISyntaxException {
		User user = TestUtils.createValidUser();
		user.setIsAdmin(0);
		Mockito.doReturn(user).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = userController.deleteUser(1,"MQ==");
		Assert.assertEquals(HttpStatus.UNAUTHORIZED, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.UNAUTHORIZED_ACCESS.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegatiiveDeleteUserTokenNotPassed() throws URISyntaxException {
		User user = TestUtils.createValidUser();
		user.setIsAdmin(0);
		Mockito.doReturn(user).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = userController.deleteUser(1,"");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegatiiveDeleteUserTokenNull() throws URISyntaxException {
		User user = TestUtils.createValidUser();
		user.setIsAdmin(0);
		Mockito.doReturn(user).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = userController.deleteUser(1,null);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.TOKEN_NOT_PASSED.getMessage(), rt.getBody().toString());
	}
	
	@Test
	public void testNegatiiveDeleteUserDoesnotExist() throws URISyntaxException {
		Mockito.doReturn(null).when(userService).getUserById(Mockito.anyInt());
		ResponseEntity<?> rt = userController.deleteUser(1,"MQ==");
		Assert.assertEquals(HttpStatus.BAD_REQUEST, rt.getStatusCode());
		Assert.assertEquals(ErrorCodeEnums.USER_DOES_NOT_EXIST.getMessage(), rt.getBody().toString());
	}

	
}
