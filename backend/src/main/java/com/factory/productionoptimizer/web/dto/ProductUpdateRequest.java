package com.factory.productionoptimizer.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record ProductUpdateRequest(
        @NotBlank String name,
        @NotNull @Positive BigDecimal price,
        @NotNull @Valid List<ProductComponentRequest> components
) {}