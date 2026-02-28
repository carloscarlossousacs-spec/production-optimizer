package com.factory.productionoptimizer.web.dto;

import java.math.BigDecimal;

public record ProductionSuggestionItemResponse(
        Long productId,
        String productCode,
        String productName,
        int quantityToProduce,
        BigDecimal totalValue
) {}