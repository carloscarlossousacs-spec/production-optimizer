package com.factory.productionoptimizer.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductComponentRequest(
        @NotNull Long rawMaterialId,
        @NotNull @Positive BigDecimal quantityRequired
) {}