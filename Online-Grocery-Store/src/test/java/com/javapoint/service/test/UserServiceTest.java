package com.javapoint.service.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.javapoint.test.utils.TestUtils;
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
		Mockito.doReturn(TestUtils.createUsersList()).when(userRepository).findAll();
		Mockito.doReturn(TestUtils.createValidUser()).when(userRepository).findOne(Mockito.anyInt());
		Mockito.doReturn(TestUtils.createValidUser()).when(userRepository).findByUserName(Mockito.anyString());
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

}
