package com.javatpoint.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.javatpoint.model.Cart;
public interface CartRepository extends JpaRepository<Cart, Integer>
{
}
