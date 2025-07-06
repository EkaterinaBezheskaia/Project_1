package api.mappers;

import api.dto.EmployeeDTO;
import org.mapstruct.Mapper;
import store.entities.EmployeeEntity;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeDTO toEmployeeDTO(EmployeeEntity employeeEntity);
    EmployeeEntity toEmployeeEntity(EmployeeDTO employeeDTO);
}
