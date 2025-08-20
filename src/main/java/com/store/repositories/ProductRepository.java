package com.store.repositories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import com.store.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer>, JpaSpecificationExecutor<ProductEntity> {

    boolean existsByName(String name);

    boolean existsByNameAndDescription(String name, String description);

    Optional<ProductEntity> findByNameAndDescription(String name, String description);
}
