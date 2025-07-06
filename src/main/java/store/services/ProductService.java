package store.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.entities.ProductEntity;
import store.repositories.ProductRepository;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductEntity addProduct(ProductEntity product) {
        return productRepository.save(product);
    }

    public ProductEntity updateProduct(ProductEntity product) {
        return productRepository.save(product);
    }

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }
}
