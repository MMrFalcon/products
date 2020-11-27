package com.falcon.products.controller;

import com.falcon.products.ProductsApplication;
import com.falcon.products.domain.Product;
import com.falcon.products.repository.ProductRepository;
import com.falcon.products.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ProductsApplication.class)
@ActiveProfiles("test")
@Transactional
class ProductControllerTestIT {

    private final String PRODUCT_NAME = "tomato33333";
    private final int PRODUCT_QUANTITY = 12;
    private Long PRODUCT_ID = 1L;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        final ProductController productController = new ProductController(productService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        entityManager.persist(Product.builder().name(PRODUCT_NAME).quantity(PRODUCT_QUANTITY).build());
        entityManager.flush();

    }

    @Test
    void getProduct() throws Exception {
        Product product = productRepository
                .save(Product.builder().name(PRODUCT_NAME).quantity(PRODUCT_QUANTITY).build());

        mockMvc.perform(get("/product/{id}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(PRODUCT_NAME))
                .andExpect(jsonPath("$.quantity").value(PRODUCT_QUANTITY));
    }

    @Test
    void addProduct() throws Exception {
        long databaseSizeBeforeAdd = StreamSupport.stream(productRepository.findAll().spliterator(), false).count();
        final String NEW_PROD_NAME = "Orange";
        final int NEW_PROD_QUANTITY = 12;
        Product productForAdd = Product.builder().name(NEW_PROD_NAME).quantity(NEW_PROD_QUANTITY).build();

        mockMvc.perform(post("/product/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(productForAdd)))
                .andExpect(status().isCreated());

        assertEquals(databaseSizeBeforeAdd + 1, StreamSupport.stream(productRepository.findAll().spliterator(), false).count());

    }

    @Test
    void createMultipleProducts() throws Exception {
        final String expectedHeaderName = "Location";
        final String expectedHeaderContent = "http://localhost:8080/product/save/async/";

        Product queuedProduct1 = Product.builder().name(PRODUCT_NAME + "1").quantity(PRODUCT_QUANTITY + 1).build();
        Product queuedProduct2 = Product.builder().name(PRODUCT_NAME + " 23").quantity(PRODUCT_QUANTITY).build();

        List<Product> productsForSave = new ArrayList<>();
        productsForSave.add(queuedProduct1);
        productsForSave.add(queuedProduct2);

        MvcResult result = mockMvc.perform(post("/product/save/async")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(productsForSave)))
                .andExpect(status().isCreated())
                .andReturn();

        assertTrue(result.getResponse().getHeaderNames().contains(expectedHeaderName));
        assertTrue(result.getResponse().getHeader(expectedHeaderName).contains(expectedHeaderContent));
    }

    @Test
    void getQueuedTasks() throws Exception {
        final String expectedHeaderName = "Location";

        Product queuedProduct1 = Product.builder().name(PRODUCT_NAME + "1").quantity(PRODUCT_QUANTITY + 1).build();
        Product queuedProduct2 = Product.builder().name(PRODUCT_NAME + " 23").quantity(PRODUCT_QUANTITY).build();

        List<Product> productsForSave = new ArrayList<>();
        productsForSave.add(queuedProduct1);
        productsForSave.add(queuedProduct2);

        MvcResult result = mockMvc.perform(post("/product/save/async")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(productsForSave)))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(get(result.getResponse().getHeader(expectedHeaderName)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].done").isBoolean())
                .andExpect(jsonPath("$.[0].cancelled").isBoolean())
                .andExpect(jsonPath("$.[0].completedExceptionally").isBoolean())
                .andExpect(jsonPath("$.[0].numberOfDependents").isNumber())
                .andExpect(jsonPath("$.[1].done").isBoolean())
                .andExpect(jsonPath("$.[1].cancelled").isBoolean())
                .andExpect(jsonPath("$.[1].completedExceptionally").isBoolean())
                .andExpect(jsonPath("$.[1].numberOfDependents").isNumber());
    }
}