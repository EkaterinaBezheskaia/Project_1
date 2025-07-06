package com.api.mappers;

import com.api.dto.EmployeeDTO;
import org.mapstruct.Mapper;
import com.store.entities.EmployeeEntity;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeDTO toEmployeeDTO(EmployeeEntity employeeEntity);
    EmployeeEntity toEmployeeEntity(EmployeeDTO employeeDTO);
}
