package com.factory.productionoptimizer.web;

import com.factory.productionoptimizer.service.ProductionPlanningService;
import com.factory.productionoptimizer.web.dto.ProductionSuggestionResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/production")
public class ProductionController {

    private final ProductionPlanningService service;

    public ProductionController(ProductionPlanningService service) {
        this.service = service;
    }

    @GetMapping("/suggest")
    public ProductionSuggestionResponse suggest() {
        return service.suggestProduction();
    }
}