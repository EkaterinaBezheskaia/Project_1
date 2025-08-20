package com.store.specifications;

import com.store.entities.ClientEntity;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ClientSpecification {

    public static Specification<ClientEntity> nameContains(String name) {
        return (root, query, cb) -> (name == null || name.isBlank())
                ? null
                : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<ClientEntity> surnameContains(String surname) {
        return (root, query, cb) -> (surname == null || surname.isBlank())
                ? null
                : cb.like(cb.lower(root.get("surname")), "%" + surname.toLowerCase() + "%");
    }

    public static Specification<ClientEntity> emailContains(String email) {
        return (root, query, cb) -> (email == null || email.isBlank())
                ? null
                : cb.like(cb.lower(root.get("emailAddress")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<ClientEntity> phoneContains(String phone) {
        return (root, query, cb) -> (phone == null || phone.isBlank())
                ? null
                : cb.like(cb.lower(root.get("phoneNumber")), "%" + phone.toLowerCase() + "%");
    }

    public static Specification<ClientEntity> hasOrders(Boolean hasOrders) {
        return (root, query, cb) -> {
            if (hasOrders == null) return null;
            return hasOrders ? cb.isNotEmpty(root.get("orders")) : cb.isEmpty(root.get("orders"));
        };
    }
}

