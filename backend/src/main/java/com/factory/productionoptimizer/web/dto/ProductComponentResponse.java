package com.factory.productionoptimizer.web.dto;

import java.math.BigDecimal;

public record ProductComponentResponse(
        Long rawMaterialId,
        String rawMaterialCode,
        String rawMaterialName,
        BigDecimal quantityRequired
) {}