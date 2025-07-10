package com.api.controllers;

import com.api.dto.ClientDTO;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import com.store.services.ClientService;

import java.util.Map;

@RestController
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/clients")
    public ClientDTO createClient(
            @RequestBody ClientDTO client) throws IllegalArgumentException {
        return clientService.createClient(client);
    }

    @PatchMapping("/clients/{id}")
    public ClientDTO updateClient(
            @RequestBody Map<String, Object> updates,
            @PathVariable int id) {
        return clientService.updateClient(id, updates);
    }

    @GetMapping("/clients")
    public Page<ClientDTO> getClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") @Pattern(regexp = "id|name|surname|emailAddress|phoneNumber") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Boolean hasOrders) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sort = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return clientService.getAllClients(name, surname, email, phone, hasOrders, pageable);
    }

    @DeleteMapping("/clients/{id}")
    public void deleteClient(
            @PathVariable int id) {
        clientService.deleteClient(id);
    }
}
