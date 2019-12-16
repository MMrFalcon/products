package com.falcon.products.controller;

import com.falcon.products.domain.Product;
import com.falcon.products.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    private final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        log.debug("Rest request to get product with id : {}", id);
        Product product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping("product/create")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        log.debug("Rest request to create product {} ", product.toString());
        Product addedProduct = productService.addProduct(product);
        return new ResponseEntity<>(addedProduct, HttpStatus.CREATED);
    }

    @PostMapping("product/create/procedure")
    public ResponseEntity<Product> addProductByProcedure(@RequestBody Product product) {
        log.debug("Rest request to create product {} by procedure", product.toString());
        Product addedProduct = productService.addProductByProcedure(product);
        return new ResponseEntity<>(addedProduct, HttpStatus.CREATED);
    }
}
