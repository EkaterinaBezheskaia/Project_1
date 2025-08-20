package com.api.mappers;

import com.api.dto.ClientDTO;
import org.mapstruct.Mapper;
import com.store.entities.ClientEntity;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderMapper.class})
public interface ClientMapper {
    @Mapping(target = "orders", source = "orders")
    ClientDTO toClientDTO(ClientEntity clientEntity);

    @Mapping(target = "orders", ignore = true)
    ClientEntity toClientEntity(ClientDTO clientDTO);
}
