package com.store.services;

import com.api.dto.EmployeeDTO;
import com.api.mappers.EmployeeMapper;
import com.store.entities.Position;
import com.store.specifications.EmployeeSpecification;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.store.entities.EmployeeEntity;
import com.store.repositories.EmployeeRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeDTO addEmployee(EmployeeDTO employee) {

        if(employeeRepository.existsByEmailAddress(employee.getEmailAddress())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email уже существует");
        }

        EmployeeEntity employeeEntity = employeeMapper.toEmployeeEntity(employee);
        return employeeMapper.toEmployeeDTO(employeeRepository.save(employeeEntity));
    }

    public EmployeeDTO updateEmployee(int id, EmployeeDTO employee) {

        EmployeeEntity employeeEntity = employeeRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Работник не найден"));

        if(employeeRepository.existsByEmailAddress(employee.getEmailAddress())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email уже существует");
        }

        return employeeMapper.toEmployeeDTO(employeeRepository.save(employeeEntity));
    }

    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(int id) {
        EmployeeEntity employeeEntity = employeeRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Сотрудник не найден"));
        return employeeMapper.toEmployeeDTO(employeeEntity);
    }

    @Transactional(readOnly = true)
    public Page<EmployeeDTO> getAllEmployees(String name, String surname, String email, Position position, Pageable pageable) {

        Specification<EmployeeEntity> spec =
                EmployeeSpecification.nameContains(name)
                        .and(EmployeeSpecification.surnameContains(surname))
                        .and(EmployeeSpecification.emailContains(email))
                        .and(EmployeeSpecification.positionEquals(position));

        return employeeRepository
                .findAll(spec, pageable)
                .map(employeeMapper::toEmployeeDTO);
    }

    public void deleteEmployee(int employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет сотрудника с запрошенным id");
        }
        employeeRepository.deleteById(employeeId);
    }

}
