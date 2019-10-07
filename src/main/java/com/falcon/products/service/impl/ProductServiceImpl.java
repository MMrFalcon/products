package com.falcon.products.service.impl;

import com.falcon.products.domain.Product;
import com.falcon.products.repository.ProductRepository;
import com.falcon.products.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void addProduct(Product product) {
        log.info("Saving " + product.toString());
        productRepository.save(product);
    }

    @Override
    public Product getProductById(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            return  optionalProduct.get();
        } else {
            throw new RuntimeException("Product not found");
        }
    }
}
