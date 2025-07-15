package com.store.repositories;

import com.store.entities.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.store.entities.EmployeeEntity;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer>, JpaSpecificationExecutor<EmployeeEntity> {
    boolean findByNameSurname(String name, String surname);

    boolean existsByEmailAddress(String value);

    boolean existsByPassword(String value);
}
