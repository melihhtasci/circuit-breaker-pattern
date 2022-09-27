package com.stonemason.catalogservice;

import com.stonemason.catalogservice.entity.Order;
import com.stonemason.catalogservice.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequestMapping("/orders")
@RestController
public class CatalogApi {

    @Autowired
    OrderRepository orderRepository;

    @PostConstruct
    public void initialOrdersTable() {
        orderRepository.saveAll(Stream.of(
                        new Order("Mobile", "electronics", "white", 20000),
                        new Order("T-Shirt", "clothes", "black", 999),
                        new Order("Jeans", "clothes", "blue", 1999),
                        new Order("Laptop", "electronics", "gray", 50000),
                        new Order("Digital watch", "electronics", "black", 2500),
                        new Order("Fan", "electronics", "black", 50000)
                ).
                collect(Collectors.toList()));
    }

    @GetMapping
    public List<Order> getOrders(){
        return orderRepository.findAll();
    }

    @GetMapping("/{category}")
    public List<Order> getOrdersByCategory(@PathVariable String category){
        return orderRepository.findByCategory(category);
    }
}
