package com.javatpoint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.javatpoint.model.OrderProduct;
public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer>
{
}
