package com.javatpoint.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.javatpoint.model.Order;
public interface OrderRepository extends JpaRepository<Order, Integer>
{

	List<Order> findAllByStatusAndEmail(String status, String email);
	List<Order> findAllByEmail(String email);
	
}
