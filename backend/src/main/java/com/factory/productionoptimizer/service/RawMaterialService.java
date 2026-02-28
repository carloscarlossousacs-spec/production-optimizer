package com.factory.productionoptimizer.service;

import com.factory.productionoptimizer.domain.RawMaterial;
import com.factory.productionoptimizer.repository.RawMaterialRepository;
import com.factory.productionoptimizer.service.exception.ConflictException;
import com.factory.productionoptimizer.service.exception.ResourceNotFoundException;
import com.factory.productionoptimizer.web.dto.RawMaterialCreateRequest;
import com.factory.productionoptimizer.web.dto.RawMaterialResponse;
import com.factory.productionoptimizer.web.dto.RawMaterialUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RawMaterialService {

    private final RawMaterialRepository repository;

    public RawMaterialService(RawMaterialRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public RawMaterialResponse create(RawMaterialCreateRequest request) {
        String code = request.code().trim();

        if (repository.existsByCode(code)) {
            throw new ConflictException("Raw material code already exists: " + code);
        }

        RawMaterial saved = repository.save(new RawMaterial(
                code,
                request.name().trim(),
                request.stockQuantity()
        ));

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<RawMaterialResponse> list() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public RawMaterialResponse update(Long id, RawMaterialUpdateRequest request) {
        RawMaterial rm = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Raw material not found: " + id)
                );

        rm.setName(request.name().trim());
        rm.setStockQuantity(request.stockQuantity());

        return toResponse(rm);
    }

    private RawMaterialResponse toResponse(RawMaterial rm) {
        return new RawMaterialResponse(
                rm.getId(),
                rm.getCode(),
                rm.getName(),
                rm.getStockQuantity()
        );
    }
}