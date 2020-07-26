package com.javapoint.service.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.javatpoint.model.User;
import com.javatpoint.repository.UserRepository;
import com.javatpoint.service.UserService;
import org.junit.Assert; 

public class UserServiceTest {
	
	@InjectMocks
	private UserService userService;
	
	@Mock
	private UserRepository userRepository;

	@Before
	public void setUp() {
		
		userService = new UserService();
		MockitoAnnotations.initMocks(this);
		Mockito.doReturn(createUsersList()).when(userRepository).findAll();
		Mockito.doReturn(createValidUser()).when(userRepository).findOne(Mockito.anyInt());
		Mockito.doReturn(createValidUser()).when(userRepository).findByUserName(Mockito.anyString());
		Mockito.doNothing().when(userRepository).delete(Mockito.anyInt());
	
	}
	
	@Test
	public void testPositiveGetAllUsers() {
		List<User> users = userService.getAllUsers();
		Assert.assertEquals(1, users.size());
	}
	
	@Test
	public void testPositiveUser() {
		User users = userService.getUserById(1);
		Assert.assertEquals(users.getUserId(), 1);
	}
	
	@Test
	public void testPositiveUserName() {
		User users = userService.getUserByName("User1");
		Assert.assertEquals(users.getUserName(), "User1");
	}
	
	@Test
	public void testPositiveDeleteUser() {
		userService.delete(1);
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
	

	private Object createUsersList() {
		List<User> users = new ArrayList<>();
		users.add(createValidUser());
		return users;
	}

}
