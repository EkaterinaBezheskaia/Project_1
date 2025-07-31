package com.api.mappers;

import com.api.dto.ClientDTO;
import org.mapstruct.Mapper;
import com.store.entities.ClientEntity;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDTO toClientDTO(ClientEntity clientEntity);

    @Mapping(target = "orders", ignore = true)
    ClientEntity toClientEntity(ClientDTO clientDTO);


}
