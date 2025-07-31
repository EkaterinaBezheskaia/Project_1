package com.store.services;

import com.api.dto.EmployeeDTO;
import com.api.mappers.EmployeeMapper;
import com.store.entities.Position;
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
import com.store.entities.EmployeeEntity;
import com.store.repositories.EmployeeRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private static final EmailValidator EMAIL_VALIDATOR_2 = EmailValidator.getInstance();
    private static final Set<Position> ALLOWED_POSITIONS =
            EnumSet.of(Position.ADMINISTRATOR, Position.MANAGER);

    public EmployeeDTO addEmployee(EmployeeDTO employee) {
        if (employeeRepository.existsByNameAndSurnameAndPosition(employee.getName(), employee.getSurname(), employee.getPosition())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Работник с таким именем и фамилией уже существует");
        }

        if (employee.getName() == null || employee.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Имя обязательно");
        }
        if (employee.getSurname() == null || employee.getSurname().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Фамилия обязательна");
        }
        if(employee.getEmailAddress() == null || employee.getEmailAddress().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email обязателен");
        }
        if (employee.getPassword() == null || employee.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пароль обязателен");
        }

        if (!employee.getName().matches("[A-Za-zА-Яа-яЁё\\s]+")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректное имя");
        }
        if (!employee.getSurname().matches("[A-Za-zА-Яа-яЁё\\s]+")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректная фамилия");
        }
        if(!EMAIL_VALIDATOR_2.isValid(employee.getEmailAddress())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректный формат email");
        }
        if (employee.getPassword().length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пароль должен быть не менее 6 символов");
        }

        if(employeeRepository.existsByEmailAddress(employee.getEmailAddress())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email уже существует");
        }

        Position pos = employee.getPosition();
        if (pos == null || !ALLOWED_POSITIONS.contains(pos)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Позиция должна быть MANAGER или ADMINISTRATOR");
        }

        EmployeeEntity employeeEntity = employeeMapper.toEmployeeEntity(employee);
        return employeeMapper.toEmployeeDTO(employeeRepository.save(employeeEntity));
    }

    public EmployeeDTO updateEmployee(int id, Map<String, String> employee) {

        EmployeeEntity employeeEntity = employeeRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Работник не найден"));

        AtomicReference<String> newName = new AtomicReference<>(employeeEntity.getName());
        AtomicReference<String> newSurname = new AtomicReference<>(employeeEntity.getSurname());
        AtomicReference<Position> newPosition  = new AtomicReference<>(employeeEntity.getPosition());

        employee.forEach((key, value) -> {
            switch (key) {
                case "name" -> {
                    if (value.trim().isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Имя не может быть пустым");
                    }
                    if (!value.trim().matches("[A-Za-zА-Яа-яЁё]+")) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректное имя");
                    }
                    newName.set(value.trim());
                }
                case "surname" -> {
                    if (value.trim().isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Фамилия не может быть пустой");
                    }
                    if (!value.trim().matches("[A-Za-zА-Яа-яЁё\\s]+")) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректная фамилия");
                    }
                    newSurname.set(value.trim());
                }
                case "email" -> {
                    if (value.trim().isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email не может быть пустым");
                    }
                    if(!EMAIL_VALIDATOR_2.isValid(value.trim())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректный формат email");
                    }
                    if(employeeRepository.existsByEmailAddress(value.trim())) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email уже существует");
                    }
                    employeeEntity.setEmailAddress(value.trim());
                }
                case "password" -> {
                    if (value.trim().isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пароль не может быть пустым");
                    }
                    if (value.trim().length() < 6 ) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пароль должен быть не менее 6 символов");
                    }
                    employeeEntity.setPassword(value.trim());
                }
                case "position" -> {
                    if (value.trim().isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Должность не может быть пустой");
                    }
                    Position pos = Position.valueOf(value.trim());
                    if (pos != Position.MANAGER && pos != Position.ADMINISTRATOR) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Должность должна быть MANAGER или ADMINISTRATOR");
                        }
                    newPosition.set(Position.valueOf(value.trim()));
                }
                default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неизвестное поле");
            }
        });

        if (employeeRepository.existsByNameAndSurnameAndPosition(newName.get(), newSurname.get(), newPosition.get())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Работник с таким ФИО и должностью уже существует");
        }

        return employeeMapper.toEmployeeDTO(employeeRepository.save(employeeEntity));
    }



    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(int id) {
        EmployeeEntity employeeEntity = employeeRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Сотрудник не найден"));
        return employeeMapper.toEmployeeDTO(employeeEntity);
    }

    @Transactional(readOnly = true)
    public Page<EmployeeDTO> getAllEmployees(String name, String surname, String email, Position position, Pageable pageable) {

        Specification<EmployeeEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            addTextPredicate(predicates, cb, root, "name", name);
            addTextPredicate(predicates, cb, root, "surname", surname);
            addTextPredicate(predicates, cb, root, "emailAddress", email);
            if (position != null) {
                predicates.add(cb.equal(root.get("position"), position));
            }

            return predicates.isEmpty()
                    ? null
                    : cb.and(predicates.toArray(new Predicate[0]));
        };

        return employeeRepository
                .findAll(spec, pageable)
                .map(employeeMapper::toEmployeeDTO);
    }

    private void addTextPredicate(List<Predicate> predicates, CriteriaBuilder cb, Root<EmployeeEntity> root, String field, String value) {
        if (StringUtils.hasText(value)) {
            predicates.add(cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%"));
        }
    }

    public void deleteEmployee(int employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет сотрудника с запрошенным id");
        }
        employeeRepository.deleteById(employeeId);
    }

}
