package com.factory.productionoptimizer.web.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(
        Long id,
        String code,
        String name,
        BigDecimal price,
        List<ProductComponentResponse> components
) {}