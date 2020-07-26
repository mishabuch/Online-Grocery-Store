package com.javatpoint.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javapoint.enums.ErrorCodeEnums;
import com.javapoint.exception.CustomException;
import com.javatpoint.model.User;
import com.javatpoint.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	
	// Get all users in the system
	public List<User> getAllUsers(){
		List<User> users = new ArrayList<>();
		userRepository.findAll().forEach(user -> users.add(user));
		return users;
	}

	// Get user details of a userid
	public User getUserById(int id) {
		return userRepository.findOne(id);
	}

	// Get user details by name
	public User getUserByName(String userName) {
		return userRepository.findByUserName(userName);
	}

	// Save or update a user
	@Transactional
	public void saveOrUpdate(User user) {
		try {
			userRepository.save(user);
		} catch (Exception e) {
			throw new CustomException(ErrorCodeEnums.COULD_NOT_SAVE_USER);
		}
	}

	// Delete a user 
	@Transactional
	public void delete(int id) {
		try {
		userRepository.delete(id);
		} catch (Exception e) {
			throw new CustomException(ErrorCodeEnums.COULD_NOT_DELETE_USER);
		}
	}
}
