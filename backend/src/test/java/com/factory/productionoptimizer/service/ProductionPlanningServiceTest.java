package com.factory.productionoptimizer.service;

import com.factory.productionoptimizer.domain.Product;
import com.factory.productionoptimizer.domain.ProductComponent;
import com.factory.productionoptimizer.domain.RawMaterial;
import com.factory.productionoptimizer.repository.ProductRepository;
import com.factory.productionoptimizer.repository.RawMaterialRepository;
import com.factory.productionoptimizer.web.dto.ProductionSuggestionResponse;
import com.factory.productionoptimizer.web.dto.ProductionSuggestionItemResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductionPlanningServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @InjectMocks
    private ProductionPlanningService service;

    @Test
    void suggestProduction_shouldPrioritizeHigherPrice_whenMaterialsConflict() {
        // Raw Material: PE stock = 10
        RawMaterial pe = new RawMaterial("RM-PE", "Polietileno (PE)", bd("10"));
        setId(pe, 1L);

        // Product A (higher price): needs 3 PE, price 40
        Product productA = new Product("P-CAIXA", "Caixa", bd("40.00"));
        setId(productA, 100L);
        productA.addComponent(new ProductComponent(pe, bd("3")));

        // Product B (lower price): needs 2 PE, price 25
        Product productB = new Product("P-BALDE", "Balde", bd("25.00"));
        setId(productB, 200L);
        productB.addComponent(new ProductComponent(pe, bd("2")));

        when(rawMaterialRepository.findAll()).thenReturn(List.of(pe));
        when(productRepository.findAll()).thenReturn(List.of(productB, productA)); // unordered on purpose

        ProductionSuggestionResponse response = service.suggestProduction();

        // Expect productA first, quantity = floor(10/3)=3, value=120
        assertEquals(bd("120.00"), response.totalValue());

        assertEquals(1, response.items().size());
        ProductionSuggestionItemResponse item = response.items().get(0);

        assertEquals("P-CAIXA", item.productCode());
        assertEquals(3, item.quantityToProduce());
        assertEquals(bd("120.00"), item.totalValue());
    }

    @Test
    void suggestProduction_shouldProduceMultipleProducts_whenDifferentMaterialsAvailable() {
        // Stocks
        RawMaterial pe = new RawMaterial("RM-PE", "Polietileno (PE)", bd("6"));
        setId(pe, 1L);

        RawMaterial col = new RawMaterial("RM-COL", "Corante Azul", bd("1"));
        setId(col, 2L);

        RawMaterial add = new RawMaterial("RM-ADD", "Aditivo UV", bd("1"));
        setId(add, 3L);

        RawMaterial pp = new RawMaterial("RM-PP", "Polipropileno (PP)", bd("1.2"));
        setId(pp, 4L);

        // Product: CAIXA price 40 needs PE 3, COL 0.15, ADD 0.05
        Product caixa = new Product("P-CAIXA", "Caixa Organizadora", bd("40.00"));
        setId(caixa, 10L);
        caixa.addComponent(new ProductComponent(pe, bd("3")));
        caixa.addComponent(new ProductComponent(col, bd("0.15")));
        caixa.addComponent(new ProductComponent(add, bd("0.05")));

        // Product: BALDE price 25 needs PE 2, COL 0.1
        Product balde = new Product("P-BALDE", "Balde 10L", bd("25.00"));
        setId(balde, 20L);
        balde.addComponent(new ProductComponent(pe, bd("2")));
        balde.addComponent(new ProductComponent(col, bd("0.1")));

        // Product: GARRAFA price 5 needs PP 0.3, ADD 0.02
        Product garrafa = new Product("P-GARRAFA", "Garrafa 2L", bd("5.00"));
        setId(garrafa, 30L);
        garrafa.addComponent(new ProductComponent(pp, bd("0.3")));
        garrafa.addComponent(new ProductComponent(add, bd("0.02")));

        when(rawMaterialRepository.findAll()).thenReturn(List.of(pe, col, add, pp));
        when(productRepository.findAll()).thenReturn(List.of(balde, garrafa, caixa)); // unordered on purpose

        ProductionSuggestionResponse response = service.suggestProduction();

        // CAIXA: max = floor(PE 6/3)=2 (others allow more) => value 80
        // Remaining PE 0, COL 0.7, ADD 0.9
        // BALDE: max = 0 because PE is 0
        // GARRAFA: max = floor(PP 1.2/0.3)=4, ADD 0.9/0.02=45 => 4 => value 20
        // Total = 100
        assertEquals(bd("100.00"), response.totalValue());
        assertEquals(2, response.items().size());

        ProductionSuggestionItemResponse first = response.items().get(0);
        assertEquals("P-CAIXA", first.productCode());
        assertEquals(2, first.quantityToProduce());
        assertEquals(bd("80.00"), first.totalValue());

        ProductionSuggestionItemResponse second = response.items().get(1);
        assertEquals("P-GARRAFA", second.productCode());
        assertEquals(4, second.quantityToProduce());
        assertEquals(bd("20.00"), second.totalValue());
    }

    // ---- helpers ----

    private static BigDecimal bd(String v) {
        return new BigDecimal(v);
    }

    private static void setId(Object entity, Long id) {
        ReflectionTestUtils.setField(entity, "id", id);
    }
}