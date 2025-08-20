package com.store.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.store.entities.ClientEntity;

import java.util.List;
import java.util.Optional;


@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Integer>, JpaSpecificationExecutor<ClientEntity> {

    boolean existsByEmailAddress(String emailAddress);

    boolean existsByPhoneNumber(String phoneNumber);

    @EntityGraph(attributePaths = {"orders", "orders.products"})
    Optional<ClientEntity> findById(int id);

    @EntityGraph(attributePaths = {"orders", "orders.products"})
    List<ClientEntity> findAll();

    @EntityGraph(attributePaths = {"orders", "orders.products"})
    Page<ClientEntity> findAll(Specification specification, Pageable pageable);

    Optional<ClientEntity> findByEmailAddress(String value);

    Optional<ClientEntity> findByPhoneNumber(String value);
}
