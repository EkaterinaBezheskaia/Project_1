package com.api.mappers;

import com.api.dto.ProductDTO;
import org.mapstruct.Mapper;
import com.store.entities.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toProductDTO (ProductEntity productEntity);
    ProductEntity toProductEntity (ProductDTO productDTO);
}
