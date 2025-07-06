package com.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.store.entities.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

}
