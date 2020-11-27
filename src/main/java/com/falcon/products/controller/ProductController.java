package com.falcon.products.controller;

import com.falcon.products.domain.Product;
import com.falcon.products.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
public class ProductController {

    private final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    private HashMap< UUID, List<CompletableFuture<Product>> > queuedTasks;

    public ProductController(ProductService productService) {
        this.productService = productService;

        this.queuedTasks = new HashMap<>();
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

    @PostMapping("product/save/async")
    public ResponseEntity<String> createMultipleProducts(@RequestBody List<Product> products) {
        log.debug("Rest request to save async multiple products {}", products);
        final UUID randomUUID = UUID.randomUUID();
        final String uri = "http://localhost:8080/product/save/async/" + randomUUID;
        List<CompletableFuture<Product>> queuedOperations = new ArrayList<>();

        products.forEach(product -> queuedOperations.add(productService.saveAsync(product)));

        queuedTasks.put(randomUUID, queuedOperations);
        log.info("Queued tasks {}", queuedTasks);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Location", uri);
        return ResponseEntity.created(URI.create(uri)).headers(headers)
                .body("Request successfully queued: " + uri);
    }

    @GetMapping("product/save/async/{taskId}")
    public ResponseEntity<List<CompletableFuture<Product>>> getQueuedTasks(@PathVariable UUID taskId) {
        log.debug("Request for get information about queued tasks with id {}", taskId);
        final List<CompletableFuture<Product>> tasks = this.queuedTasks.get(taskId);
        return ResponseEntity.ok().body(tasks);
    }
}
