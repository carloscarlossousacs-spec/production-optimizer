package com.factory.productionoptimizer.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product_components")
public class ProductComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(optional = false)
    @JoinColumn(name = "raw_material_id", nullable = false)
    private RawMaterial rawMaterial;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal quantityRequired;

    protected ProductComponent() {}

    public ProductComponent(RawMaterial rawMaterial, BigDecimal quantityRequired) {
        this.rawMaterial = rawMaterial;
        this.quantityRequired = quantityRequired;
    }

    public Long getId() { return id; }
    public Product getProduct() { return product; }
    public RawMaterial getRawMaterial() { return rawMaterial; }
    public BigDecimal getQuantityRequired() { return quantityRequired; }

    public void setProduct(Product product) { this.product = product; }
}