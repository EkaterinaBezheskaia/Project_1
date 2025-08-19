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

        if(employeeRepository.existsByEmailAddress(employee.getEmailAddress())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email уже существует");
        }

        EmployeeEntity employeeEntity = employeeMapper.toEmployeeEntity(employee);
        return employeeMapper.toEmployeeDTO(employeeRepository.save(employeeEntity));
    }

    public EmployeeDTO updateEmployee(int id, Map<String, String> employee) {

        EmployeeEntity employeeEntity = employeeRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Работник не найден"));

        employee.forEach((key, value) -> {
            if (key == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ключ не может быть null");
            if (value == null || value.trim().isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Пустое значение для " + key);
            switch (key) {
                case "name" -> employeeEntity.setName(value.trim());
                case "surname" -> employeeEntity.setSurname(value.trim());
                case "email" -> {
                    if(employeeRepository.existsByEmailAddress(value.trim()) &&
                            !employeeEntity.getEmailAddress().equals(value.trim())) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email уже существует");
                    }
                    employeeEntity.setEmailAddress(value.trim());
                }
                case "password" -> employeeEntity.setPassword(value.trim());
                case "position" -> {
                    try {
                        employeeEntity.setPosition(Position.valueOf(value.trim()));
                    } catch (IllegalArgumentException ex) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректное значение должности");
                    }
                }
                default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неизвестное поле");
            }
        });

        Optional<EmployeeEntity> sameEmployee = employeeRepository.findByNameAndSurnameAndPosition(
                employeeEntity.getName(),
                employeeEntity.getSurname(),
                employeeEntity.getPosition()
        );
        if (sameEmployee.isPresent() && sameEmployee.get().getId() != id) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Сотрудник с таким именем и фамилией уже существует");
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
