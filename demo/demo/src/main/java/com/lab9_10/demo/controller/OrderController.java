package com.lab9_10.demo.controller;

import com.lab9_10.demo.model.Order;
import com.lab9_10.demo.model.Product;
import com.lab9_10.demo.repository.OrderRepository;
import com.lab9_10.demo.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderController(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> addOrder(@RequestBody Order order) {
        for (Product product : order.getProducts()) {
            if (!productRepository.existsById(product.getId())) {
                return ResponseEntity.status(400).body("Invalid product ID: " + product.getId());
            }
        }
        return ResponseEntity.ok(orderRepository.save(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody Order newOrder) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setOrderNumber(newOrder.getOrderNumber());
                    order.setTotalPrice(newOrder.getTotalPrice());
                    order.setProducts(newOrder.getProducts());
                    return ResponseEntity.ok(orderRepository.save(order));
                })
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return ResponseEntity.ok("Order deleted successfully");
        }
        return ResponseEntity.status(404).body("Order not found");
    }
}