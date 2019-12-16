package com.falcon.products.service.impl;

import com.falcon.products.domain.Product;
import com.falcon.products.repository.ProductRepository;
import com.falcon.products.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product addProduct(Product product) {
        log.info("Saving " + product.toString());
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        } else {
            throw new RuntimeException("Product not found");
        }
    }

    @Override
    public Product addProductByProcedure(Product product) {
        log.info("Executing INSERT procedure for product {}", product.toString());
        productRepository.createProductProcedure(product.getId().intValue(), product.getName(), product.getQuantity());
        log.info("Getting last object from repository");
        List<Product> allProducts = StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return allProducts.get(allProducts.size() - 1);
    }
}
