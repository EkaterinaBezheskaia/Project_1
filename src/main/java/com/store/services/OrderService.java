package com.store.services;

import com.api.dto.OrderDTO;
import com.api.mappers.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.store.entities.OrderEntity;
import com.store.repositories.OrderRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderDTO createOrder(OrderDTO order) {
        OrderEntity orderEntity = orderMapper.toOrderEntity(order);
        OrderEntity savedOrderEntity = orderRepository.save(orderEntity);
        return orderMapper.toOrderDTO(savedOrderEntity);
    }

    public OrderDTO updateOrder(OrderDTO order) {
        OrderEntity orderEntity = orderMapper.toOrderEntity(order);
        OrderEntity savedOrderEntity = orderRepository.save(orderEntity);
        return orderMapper.toOrderDTO(savedOrderEntity);
    }

    public List<OrderDTO> getAllOrders() {
        List<OrderDTO> orderDTOS = new ArrayList<>();
        orderRepository.findAll().forEach(orderEntity -> {
            orderDTOS.add(orderMapper.toOrderDTO(orderEntity));
        });
        return orderDTOS;
    }

    public void deleteOrder(int id) {
        orderRepository.deleteById(id);
    }
}
