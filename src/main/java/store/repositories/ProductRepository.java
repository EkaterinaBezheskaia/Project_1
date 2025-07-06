package store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import store.entities.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

}
