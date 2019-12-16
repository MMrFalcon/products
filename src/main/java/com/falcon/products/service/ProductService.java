package com.falcon.products.service;

import com.falcon.products.domain.Product;

public interface ProductService {
    Product addProduct(Product product);
    Product getProductById(Long productId);
    Product addProductByProcedure(Product product);
}
