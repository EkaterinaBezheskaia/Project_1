package api.controllers;

import api.dto.OrderDTO;
import org.springframework.web.bind.annotation.*;
import store.services.OrderService;

import java.util.List;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public OrderDTO createOrder(
            @RequestBody OrderDTO order) {
        return orderService.createOrder(order);
    }

    @PatchMapping("/orders/{id}")
    public OrderDTO updateOrder(
            @PathVariable int id,
            @RequestBody OrderDTO order) {
        return orderService.updateOrder(order);
    }

    @GetMapping("/orders")
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @DeleteMapping("/orders/{id}")
    public void deleteOrder(
            @PathVariable int id) {
        orderService.deleteOrder(id);
    }
}
