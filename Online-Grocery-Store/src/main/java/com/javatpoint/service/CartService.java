package com.javatpoint.service;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javapoint.enums.ErrorCodeEnums;
import com.javapoint.exception.CustomException;
import com.javatpoint.model.Cart;
import com.javatpoint.model.Order;
import com.javatpoint.model.OrderProduct;
import com.javatpoint.model.User;
import com.javatpoint.repository.CartRepository;
import com.javatpoint.repository.OrderProductRepository;
import com.javatpoint.repository.OrderRepository;
import com.javatpoint.repository.UserRepository;

@Service
public class CartService {
	@Autowired
	ProductService productService;

	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	OrderProductRepository orderProductRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	UserService userService;

	// Get the cart related to the user
	public Cart getCart(User user) {
		return user.getCart();
	}

	@Transactional
	public void editCart(Collection<OrderProduct> entries, User user) {
		Cart finalCart = user.getCart();
		entries.forEach(entry -> {
			Set<OrderProduct> products = finalCart.getProductsInOrder();
			Optional<OrderProduct> old = products.stream().filter(e -> e.getProductId() == entry.getProductId())
					.findFirst();
			OrderProduct updatedProd = new OrderProduct();
			if (old.isPresent()) {
				updatedProd = old.get();
				updatedProd.setCount(entry.getCount() + updatedProd.getCount());
			} else {
				updatedProd = entry;
				updatedProd.setCart(finalCart);
				finalCart.getProductsInOrder().add(updatedProd);
			}
			try {
				orderProductRepository.save(updatedProd);
			} catch (Exception e1) {
				throw new CustomException(ErrorCodeEnums.COULD_NOT_ADD_PRODUCT_TO_CART);
			}
		});
		cartRepository.save(finalCart);

	}

	// Delete an iteam from the cart
	@Transactional
	public void delete(int itemId, User user) {
		Optional<OrderProduct> op = user.getCart().getProductsInOrder().stream().filter(e -> itemId == e.getProductId())
				.findFirst();
		op.ifPresent(orderProduct -> {
			orderProduct.setCart(null);
			orderProductRepository.delete(orderProduct.getId());
		});
	}

	// Create a new cart 
	// The Cart entity will have the user associated with it
	@Transactional
	public void createCart(Cart cart) {
		cartRepository.save(cart);
	}

	@Transactional
	public void checkout(User user) {
		if(user.getCart().getProductsInOrder().isEmpty()) {
			throw new CustomException(ErrorCodeEnums.CART_IS_EMPTY);
		}
		// If cart is not empty, create a new order
		Date date = new Date();
		String orderDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
		Order order = new Order(user,orderDate);
		orderRepository.save(order);

		// Empty the cart, set cart associated with products as null and set their OrderID
		// Also decrease their quantity (stock) in the inventory
		user.getCart().getProductsInOrder().forEach(productInOrder -> {
			productInOrder.setCart(null);
			productInOrder.setOrder(order);
			try {
				productService.decreaseStock(productInOrder.getProductId(), productInOrder.getCount());
			} catch (Exception e) {
				throw new CustomException(ErrorCodeEnums.COULD_NOT_DELETE_PRODUCT);
			}
			orderProductRepository.save(productInOrder);
		});
	}
}