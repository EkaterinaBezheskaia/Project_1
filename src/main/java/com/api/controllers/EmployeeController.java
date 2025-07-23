package com.api.controllers;

import com.api.dto.EmployeeDTO;
import com.store.entities.Position;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.store.services.EmployeeService;

import java.util.Map;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/employees/create")
    public EmployeeDTO addEmployee(
            @RequestBody @Valid EmployeeDTO employee) {
        return employeeService.addEmployee(employee);
    }

    @PatchMapping("/employees/update/{id}")
    public EmployeeDTO updateEmployee(
            @PathVariable("id") int id,
            @RequestBody Map<String, Object> employee) {
        return employeeService.updateEmployee(id, employee);
    }

    @GetMapping("/employees/get/{id}")
    public EmployeeDTO getEmployeeById(
            @PathVariable("id") int id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/employees/get")
    public Page<EmployeeDTO> getAllEmployees(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") @Pattern(regexp = "id|name|surname|emailAddress|position") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "acs") String direction,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "surname", required = false) String surname,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "position", required = false) Position position) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sort = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return employeeService.getAllEmployees(name, surname, email, position, pageable);
    }

    @DeleteMapping("/employees/delete/{id}")
    public void deleteEmployee(
            @PathVariable("id") int id) {
        employeeService.deleteEmployee(id);
    }
}
