package com.store.services;

import com.api.dto.EmployeeDTO;
import com.api.mappers.EmployeeMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.store.entities.EmployeeEntity;
import com.store.repositories.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeDTO addEmployee(EmployeeDTO employee) {
        EmployeeEntity employeeEntity = employeeMapper.toEmployeeEntity(employee);
        EmployeeEntity savedEmployeeEntity = employeeRepository.save(employeeEntity);
        return employeeMapper.toEmployeeDTO(savedEmployeeEntity);
    }

    public EmployeeDTO updateEmployee(EmployeeDTO employee) {
        EmployeeEntity employeeEntity = employeeMapper.toEmployeeEntity(employee);
        EmployeeEntity savedEmployeeEntity = employeeRepository.save(employeeEntity);
        return employeeMapper.toEmployeeDTO(savedEmployeeEntity);
    }

    public EmployeeDTO getEmployeeById(int id) {
        EmployeeEntity employeeEntity = employeeRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Сотрудник не найден"));
        return employeeMapper.toEmployeeDTO(employeeEntity);
    }

    public List<EmployeeDTO> getAllEmployees() {
        List<EmployeeDTO> employees = new ArrayList<>();
        employeeRepository.findAll().forEach(
                employee -> employees.add(employeeMapper.toEmployeeDTO(employee)));
        return employees;
    }

    public void deleteEmployee(int employeeId) {
        employeeRepository.deleteById(employeeId);
    }

}
