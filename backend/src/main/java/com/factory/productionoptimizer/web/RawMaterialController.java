package com.factory.productionoptimizer.web;

import com.factory.productionoptimizer.service.RawMaterialService;
import com.factory.productionoptimizer.web.dto.RawMaterialCreateRequest;
import com.factory.productionoptimizer.web.dto.RawMaterialResponse;
import com.factory.productionoptimizer.web.dto.RawMaterialUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/raw-materials")
public class RawMaterialController {

    private final RawMaterialService service;

    public RawMaterialController(RawMaterialService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RawMaterialResponse create(@Valid @RequestBody RawMaterialCreateRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<RawMaterialResponse> list() {
        return service.list();
    }

    @PutMapping("/{id}")
    public RawMaterialResponse update(@PathVariable Long id,
                                      @Valid @RequestBody RawMaterialUpdateRequest request) {
        return service.update(id, request);
    }
}