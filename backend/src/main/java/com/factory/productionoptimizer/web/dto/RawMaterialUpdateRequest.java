package com.factory.productionoptimizer.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record RawMaterialUpdateRequest(
        @NotBlank String name,
        @NotNull @PositiveOrZero BigDecimal stockQuantity
) {}