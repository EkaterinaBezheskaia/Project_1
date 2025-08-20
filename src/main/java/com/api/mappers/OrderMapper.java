package com.api.mappers;

import com.api.dto.OrderDTO;
import com.store.entities.ClientEntity;
import com.store.entities.ProductEntity;
import org.mapstruct.Mapper;
import com.store.entities.OrderEntity;
import org.mapstruct.Mapping;

import java.util.List;

//TO DO: Надо вынести логику обращения к БД в сервис!

@Mapper(componentModel = "spring", uses = ProductShortMapper.class)
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    OrderEntity toOrderEntity(OrderDTO orderDTO, ClientEntity client, List<ProductEntity> products);

    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "productsList", source = "products")
    OrderDTO toOrderDTO(OrderEntity orderEntity);
}

