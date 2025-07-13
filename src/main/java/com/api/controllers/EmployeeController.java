package com.api.controllers;

import com.api.dto.EmployeeDTO;
import org.springframework.web.bind.annotation.*;
import com.store.services.EmployeeService;

import java.util.List;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/employees/create")
    public EmployeeDTO addEmployee(
            @RequestBody EmployeeDTO employee) {
        return employeeService.addEmployee(employee);
    }

    @PatchMapping("/employees/update/{id}")
    public EmployeeDTO updateEmployee(
            @PathVariable int id,
            @RequestBody EmployeeDTO employee) {
        return employeeService.updateEmployee(employee);
    }

    @GetMapping("/employees/get/{id]")
    public EmployeeDTO getEmployeeById(
            @PathVariable int id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/employees/get")
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @DeleteMapping("/employees/delete/{id}")
    public void deleteEmployee(
            @PathVariable int id) {
        employeeService.deleteEmployee(id);
    }
}
