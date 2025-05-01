package com.app.domain.product.service;

import com.app.domain.product.entity.Product;
import com.app.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 등록
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
}
