package com.example.crudapp.service;

import com.example.crudapp.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Page<ProductDTO> searchProducts(String name, Pageable pageable);

    List<ProductDTO> getAllProducts();

    Page<ProductDTO> getAllProducts(Pageable pageable);

    ProductDTO getProductById(Long id);

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO updateProduct(Long id, ProductDTO productDTO);

    void deleteProduct(Long id);

    ProductDTO updateProductStock(Long id, int purchasedQuantity);
}
