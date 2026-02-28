package com.factory.productionoptimizer.web.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductionSuggestionResponse(
        List<ProductionSuggestionItemResponse> items,
        BigDecimal totalValue
) {}