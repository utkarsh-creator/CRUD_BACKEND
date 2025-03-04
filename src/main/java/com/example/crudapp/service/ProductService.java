package com.example.crudapp.service;

import com.example.crudapp.dto.ProductDTO;
import com.example.crudapp.exception.ResourceNotFoundException;
import com.example.crudapp.model.Product;
import com.example.crudapp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Page<ProductDTO> searchProducts(String name, Pageable pageable) {
        return productRepository.findByNameContaining(name, pageable)
                .map(product -> new ProductDTO(product.getId(), product.getName(), product.getPrice()));
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> new ProductDTO(product.getId(), product.getName(), product.getPrice()))
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return new ProductDTO(product.getId(), product.getName(), product.getPrice());
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());

        Product savedProduct = productRepository.save(product);
        return new ProductDTO(savedProduct.getId(), savedProduct.getName(), savedProduct.getPrice());
    }

    public Product updateProductStock(Long id, int purchasedQuantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (product.getQuantity() < purchasedQuantity) {
            throw new RuntimeException("Not enough stock available!");
        }

        product.setQuantity(product.getQuantity() - purchasedQuantity);
        return productRepository.save(product);
    }


    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }


    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());

        Product updatedProduct = productRepository.save(product);
        return new ProductDTO(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getPrice());
    }

}
