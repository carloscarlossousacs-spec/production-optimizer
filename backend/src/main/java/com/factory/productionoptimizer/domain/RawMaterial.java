package com.factory.productionoptimizer.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "raw_materials", uniqueConstraints = {
        @UniqueConstraint(name = "uk_raw_material_code", columnNames = "code")
})
public class RawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal stockQuantity;

    protected RawMaterial() {
        // Required by JPA
    }

    public RawMaterial(String code, String name, BigDecimal stockQuantity) {
        this.code = code;
        this.name = name;
        this.stockQuantity = stockQuantity;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getStockQuantity() {
        return stockQuantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStockQuantity(BigDecimal stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}