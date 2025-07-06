package store.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.entities.EmployeeEntity;
import store.repositories.EmployeeRepository;

import java.util.List;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeEntity addEmployee(EmployeeEntity employee) {
        return employeeRepository.save(employee);
    }

    public EmployeeEntity updateEmployee(EmployeeEntity employee) {
        return employeeRepository.save(employee);
    }

    public List<EmployeeEntity> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public void deleteEmployee(int id) {
        employeeRepository.deleteById(id);
    }
}
