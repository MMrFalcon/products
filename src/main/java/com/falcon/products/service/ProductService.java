package com.falcon.products.service;

import com.falcon.products.domain.Product;

public interface ProductService {
    void addProduct(Product product);
    Product getProductById(Long productId);
}
