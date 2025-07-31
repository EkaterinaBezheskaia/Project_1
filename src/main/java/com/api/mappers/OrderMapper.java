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

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {ClientRepository.class, ProductRepository.class})
public interface OrderMapper {

    @Mapping(target = "client", source = "clientId")
    @Mapping(target = "products", source = "productsId")
    OrderEntity toOrderEntity(OrderDTO orderDTO);

    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "productsId", expression = "java(orderEntity.getProducts().stream().map(ProductEntity::getId).collect(java.util.stream.Collectors.toList()))")
    OrderDTO toOrderDTO(OrderEntity orderEntity);

    default ClientEntity mapClientIdToEntity(int clientId, @Context ClientRepository clientRepository) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));
    }

    default List<ProductEntity> mapProductIdsToEntities(List<Integer> productIds, @Context ProductRepository productRepository) {
        List<ProductEntity> products = productRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new RuntimeException("Some products not found");
        }
        return products;
    }
}

