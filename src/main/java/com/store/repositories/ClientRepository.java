package com.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.store.entities.ClientEntity;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Integer>, JpaSpecificationExecutor<ClientEntity> {
    boolean existsByEmailAddress(String emailAddress);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByNameAndSurnameAndEmailAddressAndPhoneNumber(String name, String surname, String emailAddress, String phoneNumber);
}
