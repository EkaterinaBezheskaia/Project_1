package store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import store.entities.ClientEntity;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Integer> {
}
