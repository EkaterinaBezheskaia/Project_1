package com.store.services;

import com.api.dto.ClientDTO;
import com.api.mappers.ClientMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
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

    public ClientDTO createClient(ClientDTO client) throws IllegalArgumentException {

        if (clientRepository.existsById(client.getId())) {
            throw new IllegalArgumentException("Клиент уже существует!");
        }

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

        if (!client.getEmailAddress().matches("^[\\\\w!#$%&'*+/=?`{|}~^-]+(?:\\\\.[\\\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}$")) {
            throw new IllegalArgumentException("Некорректный формат email");
        }
        if (!client.getPhoneNumber().matches("^[0-9]{10}$")) {
            throw new IllegalArgumentException("Некорректный формат телефонного номера");
        }

        ClientEntity clientEntity = clientMapper.toClientEntity(client);
        return clientMapper.toClientDTO(clientRepository.save(clientEntity));
    }

    public ClientDTO updateClient(int id, Map<String, Object> updates) {

        ClientEntity clientEntity = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Клиент не найден"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "name" -> clientEntity.setName((String) value);
                case "surname" -> clientEntity.setSurname((String) value);
                case "emailAddress" -> {
                    if (!clientEntity.getEmailAddress().matches("^[\\\\w!#$%&'*+/=?`{|}~^-]+(?:\\\\.[\\\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}$")) {
                        throw new IllegalArgumentException("Некорректный формат email");
                    }
                    if (clientRepository.existsByEmailAddress((String) value)) {
                        throw new IllegalArgumentException("Email уже существует");
                    }
                    clientEntity.setEmailAddress((String) value);
                }
                case "phoneNumber" -> {
                    if (!clientEntity.getPhoneNumber().matches("^[0-9]{10}$")) {
                        throw new IllegalArgumentException("Некорректный формат телефонного номера");
                    }
                    if (clientRepository.existsByPhoneNumber((String) value)) {
                        throw new IllegalArgumentException("Номер телефона уже существует");
                    }
                    clientEntity.setPhoneNumber((String) value);
                }
            }
        });

        return clientMapper.toClientDTO(clientRepository.save(clientEntity));
    }

    public Page<ClientDTO> getAllClients(
            String name,
            String surname,
            String email,
            String phone,
            Boolean hasOrders,
            Pageable pageable) {

        Specification<ClientEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>(5);

            if (StringUtils.hasText(name)) {
                predicates.add(cb.like(
                        cb.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"
                ));
            }

            if (StringUtils.hasText(surname)) {
                predicates.add(cb.like(
                        cb.lower(root.get("surname")),
                        "%" + surname.toLowerCase() + "%"
                ));
            }

            if (StringUtils.hasText(email)) {
                predicates.add(cb.like(
                        cb.lower(root.get("emailAddress")),
                        "%" + email.toLowerCase() + "%"
                ));
            }

            if (StringUtils.hasText(phone)) {
                predicates.add(cb.like(
                        cb.lower(root.get("phoneNumber")),
                        "%" + phone.toLowerCase() + "%"
                ));
            }

            if (hasOrders != null) {
                if (hasOrders) {
                    predicates.add(cb.isNotEmpty(root.get("orders")));  // Есть заказы
                } else {
                    predicates.add(cb.isEmpty(root.get("orders")));  // Нет заказов
                }
            }

            return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
        };

        return clientRepository.findAll((root, query, cb) -> {
            if (query != null) {
                query.distinct(true);
            }
            return spec.toPredicate(root, query, cb);
        }, pageable).map(clientMapper::toClientDTO);
    }

    public void deleteClient(int id) {
        clientRepository.deleteById(id);
    }
}
