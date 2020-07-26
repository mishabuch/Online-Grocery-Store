package com.javatpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.javatpoint.model.Product;
import com.javatpoint.repository.ProductRepository;

@Component
public class DataLoader implements ApplicationRunner {

    private ProductRepository productRepository;

    @Autowired
    public DataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void run(ApplicationArguments args) {
    	productRepository.save(new Product("TATA Tea",100,100,"Tata","2020-10-01"));
    	productRepository.save(new Product("Dove Soap",30,100,"unilever","2020-10-01"));
    	productRepository.save(new Product("Coalgate",20,100,"Unilever","2019-10-01"));
    }
}