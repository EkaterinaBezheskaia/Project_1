package com.api.mappers;

import com.api.dto.ProductDTO;
import org.mapstruct.Mapper;
import com.store.entities.ProductEntity;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toProductDTO (ProductEntity productEntity);

    @Mapping(target = "orders", ignore = true)
    ProductEntity toProductEntity (ProductDTO productDTO);
}
