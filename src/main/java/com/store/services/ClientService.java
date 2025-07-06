package com.store.services;

import com.api.dto.ClientDTO;
import com.api.mappers.ClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.store.entities.ClientEntity;
import com.store.repositories.ClientRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientDTO createClient(ClientDTO client) {
        ClientEntity clientEntity = clientMapper.toClientEntity(client);
        ClientEntity savedClientEntity = clientRepository.save(clientEntity);
        return clientMapper.toClientDTO(savedClientEntity);
    }

    public ClientDTO updateClient(ClientDTO client) {
        ClientEntity clientEntity = clientMapper.toClientEntity(client);
        ClientEntity savedClientEntity = clientRepository.save(clientEntity);
        return clientMapper.toClientDTO(savedClientEntity);
    }

    public List<ClientDTO> getAllClients() {
        List<ClientDTO> clients = new ArrayList<>();
        clientRepository.findAll().forEach(
                clientEntity -> clients.add(clientMapper.toClientDTO(clientEntity)));
        return clients;
    }

    public void deleteClient(int id) {
        clientRepository.deleteById(id);
    }
}
