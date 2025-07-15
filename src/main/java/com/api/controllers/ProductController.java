package com.api.controllers;

import com.api.dto.ProductDTO;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import com.store.services.ProductService;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products/create")
    public ProductDTO addProduct(
            @RequestBody ProductDTO product) {
        return productService.addProduct(product);
    }

    @PatchMapping("/products/update/description/{id}")
    public ProductDTO updateProduct(
            @PathVariable int id,
            @RequestBody String description) {
        return productService.updateProduct(id, description);
    }

    @GetMapping("/products/get_all")
    public Page<ProductDTO> getAllProducts(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "id") @Pattern(regexp = "id|name|description|price") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Long price
    ) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sort = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return productService.getAllProducts(name, description, price, pageable);
    }

    @DeleteMapping("/products/delete/{id}")
    public void deleteProduct(
            @PathVariable int id) {
        productService.deleteProduct(id);
    }
}
