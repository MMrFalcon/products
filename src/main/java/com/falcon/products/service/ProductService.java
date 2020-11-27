package com.falcon.products.service;

import com.falcon.products.domain.Product;

import java.util.concurrent.CompletableFuture;

public interface ProductService {
    Product addProduct(Product product);
    Product getProductById(Long productId);
    Product addProductByProcedure(Product product);

    CompletableFuture<Product> saveAsync(Product product);
}
