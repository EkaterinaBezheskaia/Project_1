package com.api.controllers;

import com.api.dto.OrderDTO;
import com.store.entities.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import com.store.services.OrderService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

//    TO DO: fix the creation status, when it is created, it must be NEW.
    @PostMapping
    public OrderDTO createOrder(
            @RequestBody @Valid OrderDTO order) {
        return orderService.createOrder(order);
    }

    @PatchMapping("/{id}")
    public OrderDTO updateOrder(
            @PathVariable("id") int id,
            @RequestBody Status status) {
        return orderService.updateOrder(id, status);
    }

    @GetMapping
    public List<OrderDTO> getAllOrders(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") @Pattern(regexp = "id|creationDate|status") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "creationDate", required = false) LocalDate creationDate,
            @RequestParam(name = "status", required = false) Status status
            ) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sort = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return orderService.getAllOrders(creationDate, status, pageable).getContent();
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(
            @PathVariable("id") int id) {
        orderService.deleteOrder(id);
    }
}
