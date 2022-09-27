package com.stonemason.userservice;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
@RestController
@RequestMapping("/user")
public class UserServiceApplication {


	@Autowired
	RestTemplate restTemplate;

	private static final String BASE_URL = "http://localhost:8021/orders";
	private static final String USER_SERVICE = "user-service";

	@GetMapping("/get-orders")
	@CircuitBreaker(name = USER_SERVICE, fallbackMethod = "getAllAvailableOrders")
	public List<OrderDto> getOrders(@RequestParam("category") String category) {
		String url = category == null ? BASE_URL : BASE_URL + "/" + category;
		return restTemplate.getForObject(url, ArrayList.class);
	}

	public List<OrderDto> getAllAvailableOrders(String category, Exception e) {
		return Stream.of(
						new OrderDto("Mobile", "electronics", "white", 20000),
						new OrderDto("T-Shirt", "clothes", "black", 999),
						new OrderDto("Jeans", "clothes", "blue", 1999),
						new OrderDto("Laptop", "electronics", "gray", 50000),
						new OrderDto("Digital watch", "electronics", "black", 2500),
						new OrderDto("Fan", "electronics", "black", 50000)
				).
				collect(Collectors.toList());
	}

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}


}
