package api.mappers;

import api.dto.ClientDTO;
import api.dto.OrderDTO;
import api.dto.ProductDTO;
import org.mapstruct.Mapper;
import store.entities.ClientEntity;
import store.entities.OrderEntity;
import store.entities.ProductEntity;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDTO toOrderDTO(OrderEntity orderEntity);
    OrderEntity toOrderEntity(OrderDTO orderDTO);
    ClientDTO toClientDTO(ClientEntity clientEntity);
    ClientEntity toClientEntity(ClientDTO clientDTO);
    ProductDTO toProductDTO(ProductEntity productEntity);
    ProductEntity toProductEntity(ProductDTO productDTO);
}
