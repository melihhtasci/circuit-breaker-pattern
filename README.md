# Circuit Breaker Pattern

Let's imagine that we have different services and they are communicate 
each other. If one of these services cant response, it can cause a few 
errors and we dont want to happen it. It can increase sourcing easly too.

Circuit breaker works as its name and if we get error above threshold that
we specify, service state has changes (open state) and return to request
with error or info messages.

After specify timeout, Circuit breaker tests service state with incoming
requests by transmitting. According to results, changes its status. 
In the circuit breaker, there are 3 states Closed, Open, and Half-Open.

Closed: when everything is normal. Initially, the circuit breaker is in a Closed state.

Open: when a failure occurs above predetermined criteria. In this state, requests to other microservices will not be executed and fail-fast or fallback will be performed if available. When this state has passed a certain time limit, it will automatically or according to certain criteria will be returned to the Half-Open state.

Half-Open: several requests will be executed to find out whether the microservices that we are calling are working normally. If successful, the state will be returned to the Closed state. However, if it still fails it will be returned to the Open state

for more detail...

https://www.baeldung.com/spring-cloud-circuit-breaker
https://resilience4j.readme.io/docs/circuitbreaker

#### Usage

Its simple example that how to use circuit breaker. 

Catalog service urls;

Get all catalog > http://localhost:8021/orders

Get by category > http://localhost:8021/orders/{category}

In catalog api, i created sample orders in constructor.
```
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
```

There are two endpoints like i mentioned above. 

User service has an endpoint. http://localhost:8020/user/get-orders

It takes a category parameter. you can get data according to category if dont pass it as null.

```
    @GetMapping("/get-orders")
	@CircuitBreaker(name = USER_SERVICE, fallbackMethod = "getAllAvailableOrders")
	public List<OrderDto> getOrders(@RequestParam("category") String category) {
		String url = category == null ? BASE_URL : BASE_URL + "/" + category;
		return restTemplate.getForObject(url, ArrayList.class);
	}
```

There is an annotation that called CircuitBreaker. Its doing whole thing.
As you can guess from the parameter, fall back parameter works when things don't go right.

If dont go right, i specified a method for it and it returns whole data.

```
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
```

if you run projects together and call user/get-orders endpoint, you can see the orders. 

After than stop order service and call user again. It will return data from fallback method. 


