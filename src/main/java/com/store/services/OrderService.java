package com.store.services;

import com.api.dto.OrderDTO;
import com.api.dto.ProductDTO;
import com.api.dto.ProductShortDTO;
import com.api.mappers.OrderMapper;
import com.store.entities.ClientEntity;
import com.store.entities.ProductEntity;
import com.store.entities.Status;
import com.store.repositories.ClientRepository;
import com.store.repositories.ProductRepository;
import com.store.specifications.OrderSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.store.entities.OrderEntity;
import com.store.repositories.OrderRepository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final Set<Status> statusSet = EnumSet.allOf(Status.class);

    public OrderDTO createOrder(OrderDTO orderDTO) {

        ClientEntity client = clientRepository.findById(orderDTO.getClientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Клиент не найден"));

        List<Integer> productIds = orderDTO.getProductsList()
                .stream().map(ProductShortDTO::getId).toList();

        List<ProductEntity> products = productRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некоторые продукты не найдены");
        }

        Status status = orderDTO.getStatus();
        if (status == null || !statusSet.contains(status)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Статус должен быть: " + Status.listAll());
        }

        OrderEntity orderEntity = orderMapper.toOrderEntity(orderDTO, client, products);
        orderEntity.setCreationDate(LocalDateTime.now());
        orderEntity.setStatus(status);

        return orderMapper.toOrderDTO(orderRepository.save(orderEntity));
    }

    public OrderDTO updateOrder(int id, Status status) {

        OrderEntity orderEntity = orderRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Заказ не найден"));

        if (status == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Статус обязателен");
        }
        if (!statusSet.contains(status)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Статус должен быть: " + Status.listAll());
        }

        orderEntity.setStatus(status);
        return orderMapper.toOrderDTO(orderRepository.save(orderEntity));
    }

    public Page<OrderDTO> getAllOrders(LocalDate creationDate, Status status, Pageable pageable) {

        Specification<OrderEntity> spec =
                OrderSpecification.dateEquals(creationDate)
                        .and(OrderSpecification.statusEquals(status));
        return orderRepository
                .findAll(spec, pageable)
                .map(orderMapper::toOrderDTO);
    }

    public void deleteOrder(int id) {
        if (!orderRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет заказа с запрошенным id");
        }
        orderRepository.deleteById(id);
    }
}
