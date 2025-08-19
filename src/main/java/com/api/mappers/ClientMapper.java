package com.api.mappers;

import com.api.dto.ClientDTO;
import org.mapstruct.Mapper;
import com.store.entities.ClientEntity;

@Mapper(componentModel = "spring", uses = {OrderMapper.class})
public interface ClientMapper {
    ClientDTO toClientDTO(ClientEntity clientEntity);
    ClientEntity toClientEntity(ClientDTO clientDTO);
}
