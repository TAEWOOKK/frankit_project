package com.app.domain.product.service;

import com.app.domain.product.entity.Product;
import com.app.domain.product.repository.ProductRepository;
import com.app.error.ErrorType;
import com.app.error.exception.BadRequestException;
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

    public Product getProductById(Long productId) {
        return productRepository.findByProductId(productId)
                .orElseThrow(() -> new BadRequestException(ErrorType.PRODUCT_NOT_FOUND));
    }

    public void deleteProductById(Long productId) {
        productRepository.deleteById(productId);
    }
}
