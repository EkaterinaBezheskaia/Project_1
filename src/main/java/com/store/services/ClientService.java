package com.store.services;

import com.api.dto.ClientDTO;
import com.api.mappers.ClientMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.store.entities.ClientEntity;
import com.store.repositories.ClientRepository;
import org.springframework.util.StringUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();

    public ClientDTO createClient(ClientDTO client) {

        if (client.getName() == null || client.getName().isBlank()) {
            throw new IllegalArgumentException("Имя обязательно");
        }
        if (client.getSurname() == null || client.getSurname().isBlank()) {
            throw new IllegalArgumentException("Фамилия обязательна");
        }
        if (client.getEmailAddress() == null || client.getEmailAddress().isBlank()) {
            throw new IllegalArgumentException("Email обязателен");
        }
        if (client.getPhoneNumber() == null || client.getPhoneNumber().isBlank()) {
            throw new IllegalArgumentException("Телефон обязателен");
        }

        if (clientRepository.existsByEmailAddress(client.getEmailAddress())) {
            throw new IllegalArgumentException("Email уже используется");
        }
        if (clientRepository.existsByPhoneNumber(client.getPhoneNumber())) {
            throw new IllegalArgumentException("Телефон уже используется");
        }

        if (!EMAIL_VALIDATOR.isValid(client.getEmailAddress())) {
            throw new IllegalArgumentException("Некорректный формат email");
        }
        if (!client.getPhoneNumber().matches("^[0-9]{10}$")) {
            throw new IllegalArgumentException("Некорректный формат телефонного номера");
        }

        ClientEntity clientEntity = clientMapper.toClientEntity(client);
        return clientMapper.toClientDTO(clientRepository.save(clientEntity));
    }

    public ClientDTO updateClient(int id, Map<String, String> updates) {

        ClientEntity clientEntity = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Клиент не найден"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "name" -> {
                    if (value == null) {
                        throw new IllegalArgumentException("Имя не может быть пустым");
                    }
                    clientEntity.setName(value);
                }
                case "surname" -> {
                    if (value == null) {
                        throw new IllegalArgumentException("Фамилия не может быть пустой");
                    }
                    clientEntity.setSurname(value);
                }
                case "emailAddress" -> {
                    if (value == null) {
                        throw new IllegalArgumentException("Email не может быть пустым");
                    }
                    if (!EMAIL_VALIDATOR.isValid(value)) {
                        throw new IllegalArgumentException("Некорректный формат email");
                    }
                    if (clientRepository.existsByEmailAddress(value)) {
                        throw new IllegalArgumentException("Email уже существует");
                    }
                    clientEntity.setEmailAddress(value);
                }
                case "phoneNumber" -> {
                    if (value == null) {
                        throw new IllegalArgumentException("Номер телефона не может быть пустым");
                    }
                    if (!value.matches("^[0-9]{10}$")) {
                        throw new IllegalArgumentException("Некорректный формат телефонного номера");
                    }
                    if (clientRepository.existsByPhoneNumber(value)) {
                        throw new IllegalArgumentException("Номер телефона уже существует");
                    }
                    clientEntity.setPhoneNumber(value);
                }
            }
        });

        return clientMapper.toClientDTO(clientRepository.save(clientEntity));
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

    public void deleteClient(int id) {
        if (!clientRepository.existsById(id)) {
            throw new EmptyResultDataAccessException("Нет клиента с id: " + id, 1);
        }
        clientRepository.deleteById(id);
    }
}
