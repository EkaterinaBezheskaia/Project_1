package com.store.services;

import com.api.dto.EmployeeDTO;
import com.api.mappers.EmployeeMapper;
import com.store.entities.Position;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.store.entities.EmployeeEntity;
import com.store.repositories.EmployeeRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private static final EmailValidator EMAIL_VALIDATOR_2 = EmailValidator.getInstance();
    private static final Set<Position> ALLOWED_POSITIONS =
            EnumSet.of(Position.ADMINISTRATOR, Position.MANAGER);

    @ResponseStatus(HttpStatus.CONFLICT)
    public EmployeeDTO addEmployee(EmployeeDTO employee) {
        if (employeeRepository.existsByNameAndSurnameAndPosition(employee.getName(), employee.getSurname(), employee.getPosition())) {
            throw new DataIntegrityViolationException("Работник с таким именем и фамилией уже существует");
        }

        if (employee.getName() == null || employee.getName().isEmpty()) {
            throw new IllegalArgumentException("Имя обязательно");
        }
        if (employee.getSurname() == null || employee.getSurname().isEmpty()) {
            throw new IllegalArgumentException("Фамилия обязательна");
        }
        if(employee.getEmailAddress() == null || employee.getEmailAddress().isEmpty()) {
            throw new IllegalArgumentException("Email обязателен");
        }
        if (employee.getPassword() == null || employee.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Пароль обязателен");
        }

        if(!EMAIL_VALIDATOR_2.isValid(employee.getEmailAddress())) {
            throw new IllegalArgumentException("Некорректный формат email");
        }
        if (employee.getPassword().length() < 6) {
            throw new IllegalArgumentException("Пароль должен быть не менее 6 символов");
        }

        Position pos = employee.getPosition();
        if (pos == null || !ALLOWED_POSITIONS.contains(pos)) {
            throw new IllegalArgumentException(
                    "Позиция должна быть Администратором или Менеджером");
        }

        EmployeeEntity employeeEntity = employeeMapper.toEmployeeEntity(employee);
        return employeeMapper.toEmployeeDTO(employeeRepository.save(employeeEntity));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    public EmployeeDTO updateEmployee(int id, Map<String, Object> employee) {

        EmployeeEntity employeeEntity = employeeRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Работник не найден"));

        if (employeeRepository.existsByNameAndSurnameAndPosition(employeeEntity.getName(), employeeEntity.getSurname(), employeeEntity.getPosition())) {
            throw new IllegalArgumentException("Работник с таким ФИО и должностью уже существует");
        }

        employee.forEach((key, value) -> {
            switch (key) {
                case "name" -> {
                    if (value == null) {
                        throw new IllegalArgumentException("Имя не может быть пустым");
                    }
                    employeeEntity.setName((String) value);
                }
                case "surname" -> {
                    if (value == null) {
                        throw new IllegalArgumentException("Фамилия не может быть пустой");
                    }
                    employeeEntity.setSurname((String) value);
                }
                case "email" -> {
                    if (value == null) {
                        throw new IllegalArgumentException("Email не может быть пустым");
                    }
                    if(!EMAIL_VALIDATOR_2.isValid((String) value)) {
                        throw new IllegalArgumentException("Некорректный формат email");
                    }
                    if(employeeRepository.existsByEmailAddress((String) value)) {
                        throw new DataIntegrityViolationException("Email уже существует");
                    }
                    employeeEntity.setEmailAddress((String) value);
                }
                case "password" -> {
                    if (value == null) {
                        throw new IllegalArgumentException("Пароль не может быть пустым");
                    }
                    if (((String) value).length() < 6 ) {
                        throw new IllegalArgumentException("Пароль должен быть не менее 6 символов");
                    }
                    if (employeeRepository.existsByPassword((String) value)){
                        throw new DataIntegrityViolationException("Пароль уже существует");
                    }
                    employeeEntity.setPassword((String) value);
                }
                case "position" -> {
                    Position pos = (Position) value;
                    if (pos == null || !ALLOWED_POSITIONS.contains(pos)) {
                        throw new IllegalArgumentException(
                                "Позиция должна быть Администратором или Менеджером");
                    }
                    employeeEntity.setPosition((Position) value);
                }
            }
        });

        return employeeMapper.toEmployeeDTO(employeeRepository.save(employeeEntity));
    }

    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(int id) {
        EmployeeEntity employeeEntity = employeeRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Сотрудник не найден"));
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
            throw new EmptyResultDataAccessException("Нет сотрудника с id: " + employeeId, 1);
        }
        employeeRepository.deleteById(employeeId);
    }

}
