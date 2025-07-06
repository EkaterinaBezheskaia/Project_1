package store.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.entities.ClientEntity;
import store.repositories.ClientRepository;

import java.util.List;

@Service
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientEntity createClient(ClientEntity client) {
        return clientRepository.save(client);
    }

    public ClientEntity updateClient(ClientEntity client) {
        return clientRepository.save(client);
    }

    public List<ClientEntity> getAllClients() {
        return clientRepository.findAll();
    }

    public void deleteClient(int id) {
        clientRepository.deleteById(id);
    }
}
