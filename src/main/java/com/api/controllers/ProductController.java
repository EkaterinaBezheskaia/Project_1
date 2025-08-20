package com.api.controllers;

import com.api.dto.ProductDTO;
import com.store.services.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ProductDTO addProduct(
            @RequestBody @Valid ProductDTO product) {
        return productService.addProduct(product);
    }

    @PatchMapping("/{id}")
    public ProductDTO updateProduct(
            @RequestBody Map<String, String> updates,
            @PathVariable("id") int id) {
        return productService.updateProduct(id, updates);
    }

    @GetMapping
    public List<ProductDTO> getAllProducts(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") @Pattern(regexp = "id|name|description|price") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "price", required = false) Long price
    ) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sort = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return productService.getAllProducts(name, description, price, pageable)
                .getContent();
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(
            @PathVariable("id") int id) {
        productService.deleteProduct(id);
    }
}
