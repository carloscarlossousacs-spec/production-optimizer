-- ==========================
-- RAW MATERIALS
-- ==========================

INSERT INTO RAW_MATERIALS (CODE, NAME, STOCK_QUANTITY) VALUES
('RM-PE', 'Polietileno (PE)', 1000),
('RM-PP', 'Polipropileno (PP)', 800),
('RM-COL', 'Corante Azul', 50),
('RM-ADD', 'Aditivo UV', 30),
('RM-AC', 'Acrilico', 100000),
('RM-COP', 'Corante Preto', 20);

-- ==========================
-- PRODUCTS
-- ==========================

INSERT INTO PRODUCTS (CODE, NAME, PRICE) VALUES
('P-BALDE', 'Balde Plastico 10L', 25.00),
('P-CAIXA', 'Caixa Organizadora 20L', 45.00),
('P-GARRAFA', 'Garrafa PET 2L', 8.50),
('P-CADEIRA', 'Cadeira Plastica Reforcada', 120.00),
('P-TAMPA', 'Tampa Plastica Universal', 3.50),
('P-PREMIUM', 'Linha Premium Colorida', 65.00);

-- ==========================
-- PRODUCT COMPONENTS
-- ==========================

-- Balde
INSERT INTO PRODUCT_COMPONENTS (PRODUCT_ID, RAW_MATERIAL_ID, QUANTITY_REQUIRED)
VALUES (1, 2, 1.2000),
       (1, 4, 0.0500);

-- Caixa
INSERT INTO PRODUCT_COMPONENTS (PRODUCT_ID, RAW_MATERIAL_ID, QUANTITY_REQUIRED)
VALUES (2, 1, 2.5000),
       (2, 4, 0.0800);

-- Garrafa
INSERT INTO PRODUCT_COMPONENTS (PRODUCT_ID, RAW_MATERIAL_ID, QUANTITY_REQUIRED)
VALUES (3, 5, 0.3000),
       (3, 3, 0.0100);

-- Cadeira
INSERT INTO PRODUCT_COMPONENTS (PRODUCT_ID, RAW_MATERIAL_ID, QUANTITY_REQUIRED)
VALUES (4, 2, 4.0000),
       (4, 4, 0.1500);

-- Tampa
INSERT INTO PRODUCT_COMPONENTS (PRODUCT_ID, RAW_MATERIAL_ID, QUANTITY_REQUIRED)
VALUES (5, 2, 0.2000);

-- Premium
INSERT INTO PRODUCT_COMPONENTS (PRODUCT_ID, RAW_MATERIAL_ID, QUANTITY_REQUIRED)
VALUES (6, 1, 1.5000),
       (6, 3, 0.0500),
       (6, 4, 0.0400);