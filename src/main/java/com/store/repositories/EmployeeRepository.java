package com.store.repositories;

import com.store.entities.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.store.entities.EmployeeEntity;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer>, JpaSpecificationExecutor<EmployeeEntity> {

    boolean existsByEmailAddress(String value);

    boolean existsByNameAndSurnameAndPosition(String name, String surname, Position position);

    boolean existsByNameAndSurname(String name, String surname);

    Optional<EmployeeEntity> findByNameAndSurnameAndPosition(String name, String surname, Position position);
}
