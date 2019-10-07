package com.falcon.products.repository;

import com.falcon.products.domain.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
