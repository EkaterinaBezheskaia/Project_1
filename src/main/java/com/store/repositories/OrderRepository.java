package com.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.store.entities.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer>, JpaSpecificationExecutor<OrderEntity> {
}
