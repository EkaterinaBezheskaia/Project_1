package com.api.controllers;

import com.api.dto.EmployeeDTO;
import com.store.entities.Position;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import com.store.services.EmployeeService;

import java.util.List;
import java.util.Map;

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
            @RequestBody Map<String, Object> employee) {
        return employeeService.updateEmployee(id, employee);
    }

    @GetMapping("/employees/get/{id}")
    public EmployeeDTO getEmployeeById(
            @PathVariable int id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/employees/get")
    public Page<EmployeeDTO> getAllEmployees(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "id") @Pattern(regexp = "id|name|surname|emailAddress|position") String sortBy,
            @RequestParam(required = false, defaultValue = "acs") String direction,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Position position) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sort = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return employeeService.getAllEmployees(name, surname, email, position, pageable);
    }

    @DeleteMapping("/employees/delete/{id}")
    public void deleteEmployee(
            @PathVariable int id) {
        employeeService.deleteEmployee(id);
    }
}
