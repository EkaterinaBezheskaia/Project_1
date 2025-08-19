package com.api.mappers;

import com.api.dto.ProductDTO;
import com.api.dto.ProductShortDTO;
import com.store.entities.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductShortMapper {
    ProductShortDTO toProductDTO (ProductEntity productEntity);
}
