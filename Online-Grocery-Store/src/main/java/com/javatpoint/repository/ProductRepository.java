package com.javatpoint.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.javatpoint.model.Product;
public interface ProductRepository extends JpaRepository<Product, Integer>
{
	List<Product> findByNameContaining(String searchString);
}
