package com.api.mappers;

import com.api.dto.ClientDTO;
import com.api.dto.OrderDTO;
import com.api.dto.ProductDTO;
import org.mapstruct.Mapper;
import com.store.entities.ClientEntity;
import com.store.entities.OrderEntity;
import com.store.entities.ProductEntity;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDTO toOrderDTO(OrderEntity orderEntity);
    OrderEntity toOrderEntity(OrderDTO orderDTO);
    ClientDTO toClientDTO(ClientEntity clientEntity);
    ClientEntity toClientEntity(ClientDTO clientDTO);
    ProductDTO toProductDTO(ProductEntity productEntity);
    ProductEntity toProductEntity(ProductDTO productDTO);
}
