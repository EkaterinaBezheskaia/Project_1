package store.services;

import jakarta.persistence.criteria.Order;
import store.entities.OrderEntity;
import store.repositories.OrderRepository;

import java.util.List;

public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderEntity createOrder(OrderEntity order) {
        return orderRepository.save(order);
    }

    public OrderEntity updateOrder(OrderEntity order) {
        return orderRepository.save(order);
    }

    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }

    public void deleteOrder(int id) {
        orderRepository.deleteById(id);
    }
}
