package com.javatpoint.service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javatpoint.model.OrderProduct;
import com.javatpoint.model.User;
import com.javatpoint.repository.OrderProductRepository;

@Service
public class OrderProductService {
	@Autowired
	OrderProductRepository orderProductRepository;
	
	// Update an order product quantity in the cart
	@Transactional
	public void update(int itemId, Integer quantity, User user) {
		Optional<OrderProduct> op = user.getCart().getProductsInOrder().stream().filter(e -> itemId==e.getProductId()).findFirst();
        op.ifPresent(productInOrder -> {
            productInOrder.setCount(productInOrder.getCount()+quantity);
            orderProductRepository.save(productInOrder);
        });
		
	}
	
	// Find a product in the order by id
    public OrderProduct findById(int itemId, User user) {
    	Optional<OrderProduct> op = user.getCart().getProductsInOrder().stream().filter(e -> itemId==e.getProductId()).findFirst();
        AtomicReference<OrderProduct> result = new AtomicReference<>();
        op.ifPresent(result::set);
        return result.get();
    }
}