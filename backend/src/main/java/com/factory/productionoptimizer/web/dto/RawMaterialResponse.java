package com.factory.productionoptimizer.web.dto;

import java.math.BigDecimal;

public record RawMaterialResponse(
        Long id,
        String code,
        String name,
        BigDecimal stockQuantity
) {}