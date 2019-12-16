package com.falcon.products.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@NamedStoredProcedureQueries(
        @NamedStoredProcedureQuery(name = "create_product",
        procedureName = "dbo.testProductCreation",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "Id", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "Name", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "Quantity", type = Integer.class)
        })
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int quantity;

    @Builder
    public Product(Long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }
}
