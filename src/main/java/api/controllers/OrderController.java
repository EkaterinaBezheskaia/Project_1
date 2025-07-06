package api.controllers;

import jakarta.persistence.criteria.Order;
import org.springframework.web.bind.annotation.*;
import store.entities.OrderEntity;
import store.services.OrderService;

import java.util.List;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public OrderEntity createOrder(@RequestBody OrderEntity order) {
        return orderService.createOrder(order);
    }

    @PatchMapping("/orders/{id}")
    public OrderEntity updateOrder(@PathVariable int id, @RequestBody OrderEntity order) {
        return orderService.updateOrder(order);
    }

    @GetMapping("/orders")
    public List<OrderEntity> getAllOrders() {
        return orderService.getAllOrders();
    }

    @DeleteMapping("/orders/{id}")
    public void deleteOrder(@PathVariable int id) {
        orderService.deleteOrder(id);
    }
}
