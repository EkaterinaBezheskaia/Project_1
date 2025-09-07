package com.store.services;

import com.api.dto.ClientDTO;
import com.api.mappers.ClientMapper;
import com.store.specifications.ClientSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.store.entities.ClientEntity;
import com.store.repositories.ClientRepository;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @ResponseStatus(HttpStatus.CREATED)
    public ClientDTO createClient(ClientDTO client) {
        if (clientRepository.existsByEmailAddress( client.getEmailAddress())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email уже существует");
        }
        if (clientRepository.existsByPhoneNumber(client.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Номер телефона уже существует");
        }
        ClientEntity clientEntity = clientMapper.toClientEntity(client);
        return clientMapper.toClientDTO(clientRepository.save(clientEntity));
    }

    public ClientDTO updateClient(int id, ClientDTO client) {

        ClientEntity clientEntity = clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Клиент не найден"));

        if (clientRepository.existsByEmailAddress( client.getEmailAddress())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email уже существует");
        }
        if (clientRepository.existsByPhoneNumber(client.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Номер телефона уже существует");
        }

        return clientMapper.toClientDTO(clientRepository.save(clientEntity));
    }

    @Transactional(readOnly = true)
    public ClientDTO getClient(int id) {
        ClientEntity clientEntity = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Клиент не найден"));
        return clientMapper.toClientDTO(clientEntity);
    }

    @Transactional(readOnly = true)
    public Page<ClientDTO> getAllClients(
            String name, String surname, String email, String phone, Boolean hasOrders, Pageable pageable) {

        Specification<ClientEntity> spec =
                ClientSpecification.nameContains(name)
                        .and(ClientSpecification.surnameContains(surname))
                        .and(ClientSpecification.emailContains(email))
                        .and(ClientSpecification.phoneContains(phone))
                        .and(ClientSpecification.hasOrders(hasOrders));

        return clientRepository
                .findAll(spec, pageable)
                .map(clientMapper::toClientDTO);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(int id) {
        if (!clientRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет клиента с запрошенным id");
        }
        clientRepository.deleteById(id);
    }
}
