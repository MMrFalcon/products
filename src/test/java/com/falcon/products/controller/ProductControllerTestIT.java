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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ProductsApplication.class)
@ActiveProfiles("test")
@Transactional
class ProductControllerTestIT {

    private final String PRODUCT_NAME = "tomato33333";
    private final int PRODUCT_QUANTITY = 12;
    private final Long PRODUCT_ID = 1L;

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
        mockMvc.perform(get("/product/{id}", PRODUCT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productForAdd)))
                .andExpect(status().isCreated());

        assertEquals(databaseSizeBeforeAdd + 1, StreamSupport.stream(productRepository.findAll().spliterator(), false).count());

    }
}