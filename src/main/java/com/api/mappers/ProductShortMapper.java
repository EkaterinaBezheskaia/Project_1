package com.api.mappers;

import com.api.dto.ProductShortDTO;
import com.store.entities.ProductEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductShortMapper {
    ProductShortDTO toProductShortDTO(ProductEntity productEntity);

    List<ProductShortDTO> toProductShortDTO(List<ProductEntity> entities);
}
