package com.javatpoint.service;

import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.javapoint.enums.ErrorCodeEnums;
import com.javapoint.enums.StatusEnum;
import com.javapoint.exception.CustomException;
import com.javatpoint.model.Order;
import com.javatpoint.model.OrderProduct;
import com.javatpoint.model.Product;
import com.javatpoint.repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	ProductService productService;

	public Order findByOrderId(int orderId){
		return orderRepository.findOne(orderId);
	}

	@Transactional
	public ResponseEntity<?> cancelOrder(int orderId){
		Order orderMain = findByOrderId(orderId);
        if(!orderMain.getStatus().equals(StatusEnum.PLACED.getMsg())) {
            throw new CustomException(ErrorCodeEnums.CANNOT_CANCEL);
        }
        
        // Set the order status as ordered
        orderMain.setStatus(StatusEnum.CANCELED.getMsg());
        orderRepository.save(orderMain);

        // Restore Stock
        Iterable<OrderProduct> products = orderMain.getOrderProduct();
        for(OrderProduct productInOrder : products) {
            Product productInfo = productService.findById(productInOrder.getProductId());
            if(productInfo != null) {
                try {
					productService.addStock(productInOrder.getProductId(), productInOrder.getCount());
				} catch (CustomException e) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
				} catch(Exception e) {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
				}
            }
        }
        return ResponseEntity.ok(findByOrderId(orderId));
	}
	

	public List<Order> findByOrderStatusAndEmail(String status, String email){
		return orderRepository.findAllByStatusAndEmail(status,email);
	}

	public List<Order> findByUserEmail(String email){
		return orderRepository.findAllByEmail(email);
	}
	
	@Transactional
	public Order saveUpdate(Order order) {
		  return orderRepository.save(order);
	}

}