package com.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.store.entities.EmployeeEntity;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer>, JpaSpecificationExecutor<EmployeeEntity> {

    boolean existsByEmailAddress(String value);

    boolean existsByPassword(String value);

    boolean findByNameAndSurname(String name, String surname);
}
