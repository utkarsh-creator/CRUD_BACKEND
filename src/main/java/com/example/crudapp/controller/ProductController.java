//package com.example.crudapp.controller;
//
//import com.example.crudapp.dto.ProductDTO;
//import com.example.crudapp.service.ProductService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/products")
//@RequiredArgsConstructor
//public class ProductController {
//
//    private final ProductService productService;
//
//    @GetMapping
//    public ResponseEntity<List<ProductDTO>> getAllProducts() {
//        return ResponseEntity.ok(productService.getAllProducts());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
//        return ResponseEntity.ok(productService.getProductById(id));
//    }
//
//    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')") // Only ADMIN can create products
//    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
//        return ResponseEntity.ok(productService.createProduct(productDTO));
//    }
//
//    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')") // Only ADMIN can update products
//    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
//        return ResponseEntity.ok(productService.updateProduct(id, productDTO));
//    }
//
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')") // Only ADMIN can delete products
//    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
//        productService.deleteProduct(id);
//        return ResponseEntity.noContent().build();
//    }
//}
package com.example.crudapp.controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.crudapp.dto.ProductDTO;
import com.example.crudapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ✅ ADDED: Pagination for retrieving products
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    // ✅ ADDED: Search functionality for products
    @GetMapping("/search")
    public ResponseEntity<Page<ProductDTO>> searchProducts(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(productService.searchProducts(query, pageable));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.createProduct(productDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, productDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
//ProductController
//This controller manages products at the /api/products endpoint:
//
//Get All Products (GET /api/products): Retrieves products with pagination
//
//Supports pagination with page and size parameters
//
//
//Search Products (GET /api/products/search): Searches products by query
//
//Takes search query and pagination parameters
//
//
//Get Product by ID (GET /api/products/{id}): Retrieves a specific product
//Create Product (POST /api/products): Creates a new product
//
//Admin-only endpoint
//Takes product data in a ProductDTO
//
//
//Update Product (PUT /api/products/{id}): Updates a product
//
//Admin-only endpoint
//Takes product data in a ProductDTO
//
//
//Delete Product (DELETE /api/products/{id}): Removes a product
//
//Admin-only endpoint