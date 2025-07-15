package com.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.store.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer>, JpaSpecificationExecutor<ProductEntity> {

    boolean existByName(String name);
}
