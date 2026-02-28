package com.factory.productionoptimizer.service;

import com.factory.productionoptimizer.domain.Product;
import com.factory.productionoptimizer.domain.ProductComponent;
import com.factory.productionoptimizer.domain.RawMaterial;
import com.factory.productionoptimizer.repository.ProductRepository;
import com.factory.productionoptimizer.repository.RawMaterialRepository;
import com.factory.productionoptimizer.service.exception.ConflictException;
import com.factory.productionoptimizer.service.exception.ResourceNotFoundException;
import com.factory.productionoptimizer.web.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final RawMaterialRepository rawMaterialRepository;

	public ProductService(ProductRepository productRepository, RawMaterialRepository rawMaterialRepository) {
		this.productRepository = productRepository;
		this.rawMaterialRepository = rawMaterialRepository;
	}

	@Transactional
	public ProductResponse create(ProductCreateRequest request) {
		String code = request.code().trim();

		if (productRepository.existsByCode(code)) {
			throw new ConflictException("Product code already exists: " + code);
		}

		Product product = new Product(code, request.name().trim(), request.price());

		for (ProductComponentRequest c : request.components()) {
			RawMaterial rm = rawMaterialRepository.findById(c.rawMaterialId())
					.orElseThrow(() -> new ResourceNotFoundException("Raw material not found: " + c.rawMaterialId()));

			ProductComponent component = new ProductComponent(rm, c.quantityRequired());
			product.addComponent(component);
		}

		Product saved = productRepository.save(product);
		return toResponse(saved);
	}

	@Transactional(readOnly = true)
	public ProductResponse getById(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
		return toResponse(product);
	}

	@Transactional(readOnly = true)
	public List<ProductResponse> list() {
		return productRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Transactional
	public ProductResponse update(Long id, ProductUpdateRequest request) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));

		product.setName(request.name().trim());
		product.setPrice(request.price());

		// Clear and rebuild components
		product.getComponents().clear();

		for (ProductComponentRequest c : request.components()) {
			RawMaterial rm = rawMaterialRepository.findById(c.rawMaterialId())
					.orElseThrow(() -> new ResourceNotFoundException("Raw material not found: " + c.rawMaterialId()));

			ProductComponent component = new ProductComponent(rm, c.quantityRequired());
			product.addComponent(component);
		}

		return toResponse(product);
	}

	private ProductResponse toResponse(Product p) {
		List<ProductComponentResponse> components = p
				.getComponents().stream().map(pc -> new ProductComponentResponse(pc.getRawMaterial().getId(),
						pc.getRawMaterial().getCode(), pc.getRawMaterial().getName(), pc.getQuantityRequired()))
				.toList();

		return new ProductResponse(p.getId(), p.getCode(), p.getName(), p.getPrice(), components);
	}
}