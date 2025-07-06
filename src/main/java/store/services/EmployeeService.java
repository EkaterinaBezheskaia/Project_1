package store.services;

import api.dto.EmployeeDTO;
import api.mappers.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.entities.EmployeeEntity;
import store.repositories.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;

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
