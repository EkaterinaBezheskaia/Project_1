package store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import store.entities.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
}
