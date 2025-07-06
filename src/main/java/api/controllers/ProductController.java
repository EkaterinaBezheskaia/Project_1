package api.controllers;

import api.dto.ProductDTO;
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
    public ProductDTO addProduct(
            @RequestBody ProductDTO product) {
        return productService.addProduct(product);
    }

    @PatchMapping("/products/{id}")
    public ProductDTO updateProduct(
            @PathVariable int id,
            @RequestBody ProductDTO product) {
        return productService.updateProduct(product);
    }

    @GetMapping("/products/all")
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(
            @PathVariable int id) {
        productService.deleteProduct(id);
    }
}
