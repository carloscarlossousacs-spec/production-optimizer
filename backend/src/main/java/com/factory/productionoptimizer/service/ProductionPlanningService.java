package com.factory.productionoptimizer.service;

import com.factory.productionoptimizer.domain.Product;
import com.factory.productionoptimizer.domain.ProductComponent;
import com.factory.productionoptimizer.domain.RawMaterial;
import com.factory.productionoptimizer.repository.ProductRepository;
import com.factory.productionoptimizer.repository.RawMaterialRepository;
import com.factory.productionoptimizer.web.dto.*;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class ProductionPlanningService {

    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;

    public ProductionPlanningService(ProductRepository productRepository,
                                     RawMaterialRepository rawMaterialRepository) {
        this.productRepository = productRepository;
        this.rawMaterialRepository = rawMaterialRepository;
    }

    public ProductionSuggestionResponse suggestProduction() {

        // IMPORTANT: make a mutable copy because repository may return an unmodifiable list
        List<Product> products = new ArrayList<>(productRepository.findAll());
        products.sort(Comparator.comparing(Product::getPrice).reversed());

        // Copy current stock to a mutable map (virtual stock)
        Map<Long, BigDecimal> availableStock = new HashMap<>();
        for (RawMaterial rm : rawMaterialRepository.findAll()) {
            availableStock.put(rm.getId(), rm.getStockQuantity());
        }

        List<ProductionSuggestionItemResponse> items = new ArrayList<>();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (Product product : products) {
            int maxQuantity = calculateMaxQuantity(product, availableStock);

            if (maxQuantity <= 0) {
                continue;
            }

            // Consume virtual stock
            for (ProductComponent component : product.getComponents()) {
                Long rmId = component.getRawMaterial().getId();

                BigDecimal requiredTotal = component.getQuantityRequired()
                        .multiply(BigDecimal.valueOf(maxQuantity));

                BigDecimal current = availableStock.getOrDefault(rmId, BigDecimal.ZERO);
                availableStock.put(rmId, current.subtract(requiredTotal));
            }

            BigDecimal productTotalValue = product.getPrice()
                    .multiply(BigDecimal.valueOf(maxQuantity));

            totalValue = totalValue.add(productTotalValue);

            items.add(new ProductionSuggestionItemResponse(
                    product.getId(),
                    product.getCode(),
                    product.getName(),
                    maxQuantity,
                    productTotalValue
            ));
        }

        return new ProductionSuggestionResponse(items, totalValue);
    }

    private int calculateMaxQuantity(Product product, Map<Long, BigDecimal> availableStock) {

        if (product.getComponents() == null || product.getComponents().isEmpty()) {
            return 0;
        }

        int max = Integer.MAX_VALUE;

        for (ProductComponent component : product.getComponents()) {

            BigDecimal perUnit = component.getQuantityRequired();
            if (perUnit == null || perUnit.compareTo(BigDecimal.ZERO) <= 0) {
                // invalid composition entry; safest is to block production
                return 0;
            }

            Long rmId = component.getRawMaterial().getId();
            BigDecimal available = availableStock.getOrDefault(rmId, BigDecimal.ZERO);

            // floor(available / perUnit)
            BigDecimal possible = available.divide(perUnit, 0, RoundingMode.DOWN);

            // guard for very large numbers, but realistic use is fine
            int possibleInt = possible.min(BigDecimal.valueOf(Integer.MAX_VALUE)).intValue();

            max = Math.min(max, possibleInt);
        }

        return max == Integer.MAX_VALUE ? 0 : max;
    }
}