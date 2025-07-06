package api.mappers;

import api.dto.ClientDTO;
import org.mapstruct.Mapper;
import store.entities.ClientEntity;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDTO toClientDTO(ClientEntity clientEntity);
    ClientEntity toClientEntity(ClientDTO clientDTO);
}
