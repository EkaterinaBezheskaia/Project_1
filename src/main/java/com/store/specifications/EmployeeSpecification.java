package com.store.specifications;

import com.store.entities.EmployeeEntity;
import com.store.entities.Position;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class EmployeeSpecification {
    public static Specification<EmployeeEntity> nameContains(String name) {
        return (root, query, cb) -> (name == null || name.isBlank())
                ? null
                : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<EmployeeEntity> surnameContains(String surname) {
        return (root, query, cb) -> (surname == null || surname.isBlank())
                ? null
                : cb.like(cb.lower(root.get("surname")), "%" + surname.toLowerCase() + "%");
    }

    public static Specification<EmployeeEntity> emailContains(String email) {
        return (root, query, cb) -> (email == null || email.isBlank())
                ? null
                : cb.like(cb.lower(root.get("emailAddress")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<EmployeeEntity> positionEquals(Position position) {
        return (root, query, cb) -> (position == null)
                ? null
                : cb.equal(root.get("position"), position);
    }

}
