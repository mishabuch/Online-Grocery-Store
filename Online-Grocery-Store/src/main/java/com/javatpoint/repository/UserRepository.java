package com.javatpoint.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.javatpoint.model.User;
public interface UserRepository extends JpaRepository<User, Integer>
{
	User findByUserName(String userName);
}
