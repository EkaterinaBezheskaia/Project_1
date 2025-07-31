package com.api.mappers;

import com.api.dto.ClientDTO;
import com.api.dto.OrderDTO;
import com.api.dto.ProductDTO;
import com.store.repositories.ClientRepository;
import com.store.repositories.ProductRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import com.store.entities.ClientEntity;
import com.store.entities.OrderEntity;
import com.store.entities.ProductEntity;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class OrderMapper {

    @Autowired
    protected ClientRepository clientRepository;

    @Autowired
    protected ProductRepository productRepository;

    // Маппинг из DTO в Entity
    @Mapping(target = "client", expression = "java(mapClientIdToEntity(orderDTO.getClientId()))")
    @Mapping(target = "products", expression = "java(mapProductIdsToEntities(orderDTO.getProductsId()))")
    public abstract OrderEntity toOrderEntity(OrderDTO orderDTO);

    // Маппинг из Entity в DTO
    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "productsId", expression = "java(toProductIds(orderEntity.getProducts()))")
    public abstract OrderDTO toOrderDTO(OrderEntity orderEntity);

    // Вспомогательные методы
    protected ClientEntity mapClientIdToEntity(int clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    protected List<ProductEntity> mapProductIdsToEntities(List<Integer> productIds) {
        List<ProductEntity> products = productRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new RuntimeException("Some products not found");
        }
        return products;
    }

    protected List<Integer> toProductIds(List<ProductEntity> products) {
        return products.stream()
                .map(ProductEntity::getId)
                .toList();
    }
}

