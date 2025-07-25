package com.store.services;

import com.api.dto.ClientDTO;
import com.api.mappers.ClientMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
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
    private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();


    @ResponseStatus(HttpStatus.CONFLICT)
    public ClientDTO createClient(ClientDTO client) {
        if (clientRepository.existsByNameAndSurnameAndEmailAddressAndPhoneNumber(client.getName(), client.getSurname(), client.getEmailAddress(), client.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Клиент уже существует");
        }
        if (client.getName() == null || client.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Имя обязательно");
        }
        if (client.getSurname() == null || client.getSurname().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Фамилия обязательна");
        }
        if (client.getEmailAddress() == null || client.getEmailAddress().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email обязателен");
        }
        if (client.getPhoneNumber() == null || client.getPhoneNumber().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Телефон обязателен");
        }

        if (clientRepository.existsByEmailAddress(client.getEmailAddress())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email уже используется");
        }
        if (clientRepository.existsByPhoneNumber(client.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Телефон уже используется");
        }

        if (!client.getName().matches("[A-Za-zА-Яа-я]")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректное имя");
        }
        if (!client.getSurname().matches("[A-Za-zА-Яа-я]")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректная фамилия");
        }
        if (!EMAIL_VALIDATOR.isValid(client.getEmailAddress())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректный формат email");
        }
        if (!client.getPhoneNumber().matches("^[0-9]{10}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректный формат телефонного номера");
        }

        ClientEntity clientEntity = clientMapper.toClientEntity(client);
        return clientMapper.toClientDTO(clientRepository.saveAndFlush(clientEntity));
    }

    public ClientDTO updateClient(int id, Map<String, String> updates) {

        ClientEntity clientEntity = clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Клиент не найден"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "name" -> {
                    if (value == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Имя не может быть пустым");
                    }
                    if (!value.matches("[A-Za-zА-Яа-я]")) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректное имя");
                    }
                    clientEntity.setName(value);
                }
                case "surname" -> {
                    if (value == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Фамилия не может быть пустой");
                    }
                    if (!value.matches("[A-Za-zА-Яа-я]")) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректная фамилия");
                    }
                    clientEntity.setSurname(value);
                }
                case "emailAddress" -> {
                    if (value == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email не может быть пустым");
                    }
                    if (!EMAIL_VALIDATOR.isValid(value)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректный формат email");
                    }
                    if (clientRepository.existsByEmailAddress(value)) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email уже существует");
                    }
                    clientEntity.setEmailAddress(value);
                }
                case "phoneNumber" -> {
                    if (value == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Номер телефона не может быть пустым");
                    }
                    if (!value.matches("^[0-9]{10}$")) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректный формат телефонного номера");
                    }
                    if (clientRepository.existsByPhoneNumber(value)) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Номер телефона уже существует");
                    }
                    clientEntity.setPhoneNumber(value);
                }
            }
        });

        return clientMapper.toClientDTO(clientRepository.saveAndFlush(clientEntity));
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет клиента с запрошенным id");
        }
        clientRepository.deleteById(id);
    }
}
