package api.controllers;

import org.springframework.web.bind.annotation.*;
import store.entities.EmployeeEntity;
import store.services.EmployeeService;

import java.util.List;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/employees")
    public EmployeeEntity addEmployee(
            @RequestBody EmployeeEntity employee) {
        return employeeService.addEmployee(employee);
    }

    @PatchMapping("/employees/{id}")
    public EmployeeEntity updateEmployee(
            @PathVariable int id,
            @RequestBody EmployeeEntity employee) {
        return employeeService.updateEmployee(employee);
    }

    @GetMapping("/employees")
    public List<EmployeeEntity> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @DeleteMapping("/employees/{id}")
    public void deleteEmployee(
            @PathVariable int id) {
        employeeService.deleteEmployee(id);
    }
}
