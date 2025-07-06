package api.mappers;

import api.dto.ProductDTO;
import org.mapstruct.Mapper;
import store.entities.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toProductDTO (ProductEntity productEntity);
    ProductEntity toProductEntity (ProductDTO productDTO);
}
