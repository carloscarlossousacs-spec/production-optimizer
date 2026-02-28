package com.factory.productionoptimizer.repository;

import com.factory.productionoptimizer.domain.ProductComponent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductComponentRepository extends JpaRepository<ProductComponent, Long> {
}