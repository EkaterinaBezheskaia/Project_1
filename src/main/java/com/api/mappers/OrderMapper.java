package com.api.mappers;

import com.api.dto.OrderDTO;
import org.mapstruct.Mapper;
import com.store.entities.OrderEntity;

//TO DO: Надо вынести логику обращения к БД в сервис!

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderEntity toOrderEntity(OrderDTO orderDTO);
    OrderDTO toOrderDTO(OrderEntity orderEntity);
}

