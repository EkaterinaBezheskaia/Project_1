package com.store.services;

import com.api.dto.ClientDTO;
import com.api.mappers.ClientMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.store.entities.ClientEntity;
import com.store.repositories.ClientRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number уже существует");
        }
        ClientEntity clientEntity = clientMapper.toClientEntity(client);
        return clientMapper.toClientDTO(clientRepository.save(clientEntity));
    }

    public ClientDTO updateClient(int id, Map<String, String> updates) {

        ClientEntity clientEntity = clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Клиент не найден"));

        updates.forEach((key, value) -> {
            if (key == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ключ не может быть null");
            }
            switch (key) {
                case "name" -> {
                    if (value == null || value.isBlank())
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Имя не может быть пустым");
                    clientEntity.setName(value);
                }
                case "surname" -> {
                    if (value == null || value.isBlank())
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Фамилия не может быть пустой");
                    clientEntity.setSurname(value);
                }
                case "emailAddress" -> {
                    if (value == null || value.isBlank())
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email не может быть пустым");
                    if (clientRepository.existsByEmailAddress(value) && !value.equals(clientEntity.getEmailAddress()))
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email уже используется");
                    clientEntity.setEmailAddress(value);
                }
                case "phoneNumber" -> {
                    if (value == null || value.isBlank())
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Телефон не может быть пустым");
                    if (clientRepository.existsByPhoneNumber(value) && !value.equals(clientEntity.getPhoneNumber()))
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Телефон уже используется");
                    clientEntity.setPhoneNumber(value);
                }
                default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неизвестное поле: " + key);
            }
        });

        return clientMapper.toClientDTO(clientRepository.save(clientEntity));
    }

    @Transactional(readOnly = true)
    public ClientDTO getClient(int id) {
        ClientEntity clientEntity = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Клиент не найден"));
        return clientMapper.toClientDTO(clientEntity);
    }

    @Transactional(readOnly = true)
    public Page<ClientDTO> getAllClients(String name, String surname, String email, String phone, Boolean hasOrders, Pageable pageable) {

        Specification<ClientEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            addTextPredicate(predicates, cb, root, "name", name);
            addTextPredicate(predicates, cb, root, "surname", surname);
            addTextPredicate(predicates, cb, root, "emailAddress", email);
            addTextPredicate(predicates, cb, root, "phoneNumber", phone);

            if (hasOrders != null) {
                predicates.add(hasOrders
                        ? cb.isNotEmpty(root.get("orders"))
                        : cb.isEmpty(root.get("orders")));
            }

            return predicates.isEmpty()
                    ? null
                    : cb.and(predicates.toArray(new Predicate[0]));
        };

        return clientRepository
                .findAll(spec, pageable)
                .map(clientMapper::toClientDTO);
    }

    private void addTextPredicate(List<Predicate> predicates, CriteriaBuilder cb, Root<ClientEntity> root, String field, String value) {
        if (StringUtils.hasText(value)) {
            predicates.add(cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%"));
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(int id) {
        if (!clientRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет клиента с запрошенным id");
        }
        clientRepository.deleteById(id);
    }
}
