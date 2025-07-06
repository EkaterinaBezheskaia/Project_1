package api.controllers;

import org.springframework.web.bind.annotation.*;
import store.entities.ProductEntity;
import store.services.ProductService;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public ProductEntity addProduct(
            @RequestBody ProductEntity product) {
        return productService.addProduct(product);
    }

    @PatchMapping("/products/{id}")
    public ProductEntity updateProduct(
            @PathVariable int id,
            @RequestBody ProductEntity product) {
        return productService.updateProduct(product);
    }

    @GetMapping("/products/all")
    public List<ProductEntity> getAllProducts() {
        return productService.getAllProducts();
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(
            @PathVariable int id) {
        productService.deleteProduct(id);
    }
}
