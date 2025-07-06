package api.mappers;

import api.dto.OrderDTO;
import org.mapstruct.Mapper;
import store.entities.OrderEntity;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDTO toOrderDTO(OrderEntity orderEntity);
    OrderEntity toOrderEntity(OrderDTO orderDTO);
}
