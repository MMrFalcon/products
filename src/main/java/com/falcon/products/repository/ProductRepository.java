package com.falcon.products.repository;

import com.falcon.products.domain.Product;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    @Procedure(name = "create_product")
    void createProductProcedure(@Param("Id") Integer id, @Param("Name") String name, @Param("Quantity") Integer quantity);

}
