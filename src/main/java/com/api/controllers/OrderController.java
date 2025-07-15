package com.api.controllers;

import com.api.dto.OrderDTO;
import com.store.entities.Status;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import com.store.services.OrderService;

import java.time.Instant;
import java.util.List;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders/create")
    public OrderDTO createOrder(
            @RequestBody OrderDTO order) {
        return orderService.createOrder(order);
    }

    @PatchMapping("/orders/update/{id}")
    public OrderDTO updateOrder(
            @PathVariable int id,
            @RequestBody Status status) {
        return orderService.updateOrder(id, status);
    }

    @GetMapping("/orders/get_all")
    public Page<OrderDTO> getAllOrders(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "id") @Pattern(regexp = "id|createdAt|status") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false) Instant createdAt,
            @RequestParam(required = false) Status status
            ) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sort = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return orderService.getAllOrders(createdAt, status, pageable);
    }

    @DeleteMapping("/orders/delete/{id}")
    public void deleteOrder(
            @PathVariable int id) {
        orderService.deleteOrder(id);
    }
}
