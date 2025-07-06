package api.controllers;

import ch.qos.logback.core.net.server.Client;
import org.springframework.web.bind.annotation.*;
import store.entities.ClientEntity;
import store.services.ClientService;

import java.util.List;

@RestController
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/clients")
    public ClientEntity createClient(
            @RequestBody ClientEntity client) {
        return clientService.createClient(client);
    }

    @PatchMapping("/clients/{id}")
    public ClientEntity updateClient(
            @RequestBody ClientEntity client,
            @PathVariable int id) {
        return clientService.updateClient(client);
    }

    @GetMapping("/clients")
    public List<ClientEntity> getAllClients() {
        return clientService.getAllClients();
    }

    @DeleteMapping("/clients/{id}")
    public void deleteClient(
            @PathVariable int id) {
        clientService.deleteClient(id);
    }
}
