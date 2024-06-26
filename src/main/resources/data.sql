INSERT INTO ROLE (ROLE_ID, NAME)
VALUES
    (11, 'ROLE_USER'),
    (12, 'ROLE_ADMIN'),
    (13, 'ROLE_PURCHASER'),
    (14, 'ROLE_QA');

INSERT INTO LOCATION (ID, NAME)
VALUES
    (11, 'Shanghai'),
    (12, 'Paderborn'),
    (13, 'Salzburg'),
    (14, 'Vienna'),
    (15, 'Cologne'),
    (16, 'Rio'),
    (17, 'Trier');

INSERT INTO USERS (ID, LOCATION_ID, FIRST_NAME, LAST_NAME, MAIL, PASSWORD, USERNAME, IS_ACTIVE, CREATED_BY_ID, CREATED_AT)
VALUES
    (11, 11, null, null, 'bill@test.de', '$2a$10$UfUHZsNiorgvWlLig3y.DODxo0bRyGPCSptgkKgzW3dDDlIUkb5a6', 'bill', true, 12, '2023-07-09 17:17:49.45023'),
    (12, 12, null, null, 'bob@test.de', '$2a$10$UfUHZsNiorgvWlLig3y.DODxo0bRyGPCSptgkKgzW3dDDlIUkb5a6', 'bob', true, 11, '2023-07-09 17:17:49.45023');

INSERT INTO USER_ROLES (ROLE_ID, USER_ID)
VALUES
    (12, 11),
    (11, 12);

INSERT INTO DEFECT_STATUS (ID, NAME)
VALUES
    (11, 'New'),
    (12, 'QA check'),
    (13, 'Disposed'),
    (14, 'Cleared');

INSERT INTO DEFECT_TYPE (ID, NAME)
VALUES
    (11, 'Foreign Body'),
    (12, 'Pest'),
    (13, 'Quality Issue'),
    (14, 'Mold'),
    (15, 'Undefined');

INSERT INTO CAUSATION_CATEGORY (ID, NAME)
VALUES
    (11, 'Undefined'),
    (12, 'Supplier-caused'),
    (13, 'Internal error'),
    (14, 'False report');

INSERT INTO PROCESS (ID, NAME)
VALUES
    (11, 'Production'),
    (12, 'Logistics'),
    (13, 'Maintenance'),
    (14, 'Warehousing');

INSERT INTO MATERIAL (ID, NAME)
VALUES
    (11, 'Sage'),
    (12, 'Strawberries'),
    (13, 'Peppermint'),
    (14, 'Chamomile'),
    (15, 'Cinnamon'),
    (16, 'Lavender'),
    (17, 'Rosemary'),
    (18, 'Basil'),
    (19, 'Mint'),
    (20, 'Oregano'),
    (21, 'Thyme'),
    (22, 'Garlic'),
    (23, 'Ginger'),
    (24, 'Turmeric'),
    (25, 'Paprika'),
    (26, 'Clove'),
    (27, 'Nutmeg'),
    (28, 'Cardamom'),
    (29, 'Saffron'),
    (30, 'Vanilla'),
    (31, 'Parsley'),
    (32, 'Dill'),
    (33, 'Chives'),
    (34, 'Bay Leaves'),
    (35, 'Cilantro'),
    (36, 'Fennel'),
    (37, 'Lemongrass'),
    (38, 'Kale'),
    (39, 'Spinach'),
    (40, 'Carrots'),
    (41, 'Tomatoes'),
    (42, 'Cucumbers'),
    (43, 'Bell Peppers'),
    (44, 'Onions'),
    (45, 'Leeks'),
    (46, 'Scallions'),
    (47, 'Radishes'),
    (48, 'Beets'),
    (49, 'Pumpkin'),
    (50, 'Zucchini'),
    (51, 'Eggplant'),
    (52, 'Mushrooms'),
    (53, 'Lentils'),
    (54, 'Chickpeas'),
    (55, 'Black Beans'),
    (56, 'Kidney Beans'),
    (57, 'Quinoa'),
    (58, 'Oats'),
    (59, 'Barley'),
    (60, 'Brown Rice'),
    (61, 'Corn'),
    (62, 'Wheat'),
    (63, 'Rye'),
    (64, 'Almonds'),
    (65, 'Walnuts'),
    (66, 'Pecans');


INSERT INTO MATERIAL_RESPONSIBLE_USERS (MATERIAL_ID, USER_ID)
VALUES
    (13, 11),
    (12, 12),
    (11, 11),
    (14, 12),
    (15, 12),
    (16, 11),
    (17, 11),
    (18, 11),
    (19, 11),
    (20, 11),
    (21, 11),
    (22, 11),
    (23, 11),
    (24, 11),
    (25, 11),
    (26, 11),
    (27, 11),
    (28, 11),
    (29, 11),
    (30, 11),
    (31, 11),
    (32, 11),
    (33, 11),
    (34, 12),
    (35, 12),
    (36, 11),
    (37, 11),
    (38, 11),
    (39, 11),
    (40, 11),
    (41, 11),
    (42, 11),
    (43, 12),
    (44, 12),
    (45, 11),
    (46, 11),
    (47, 11),
    (48, 11),
    (49, 11),
    (50, 11),
    (51, 11),
    (52, 11),
    (53, 11),
    (54, 11),
    (55, 12),
    (56, 12),
    (57, 11),
    (58, 11),
    (59, 11),
    (60, 12),
    (61, 12),
    (62, 11),
    (63, 11),
    (64, 11),
    (65, 11),
    (66, 11);

INSERT INTO SUPPLIER (ID, NAME)
VALUES
    (11, 'Acme Supply Co.'),
    (12, 'Global Trade Ltd.'),
    (13, 'Prime Wholesale Inc.'),
    (14, 'Oceanic Enterprises'),
    (15, 'Summit Logistics'),
    (16, 'Pinnacle Distributors');

INSERT INTO LOT (ID, MATERIAL_ID, SUPPLIER_ID, LOT_NUMBER)
VALUES
    (11, 11, 11, 'x0815x'),
    (12, 12, 12, '0816'),
    (13, 12, 12, '0817'),
    (14, 11, 11, '0818');

INSERT INTO DEFECT (CREATED_BY_ID, DESCRIPTION, DEFECT_STATUS_ID, CAUSATION_CATEGORY_ID, DEFECT_TYPE_ID, ID, LOCATION_ID, LOT_ID, PROCESS_ID, CREATED_AT, CHANGED_BY_ID, CHANGED_AT)
VALUES
    (11,'Test Description1', 11, 11, 11, 11, 11, 11, 11, '2023-07-09 17:17:49.45023', 11,'2023-07-10 17:17:49.45023'),
    (11,'Test Description2', 12, 11, 12, 12, 12, 12, 12, '2023-04-09 17:17:49.45024', 11,'2023-07-11 17:17:49.45023'),
    (11,'Test Description3', 13, 12, 13, 13, 13, 13, 11, '2023-01-09 17:17:49.45025', 11,'2023-07-12 17:17:49.45023'),
    (12,'Test Description4', 14, 12, 11, 14, 11, 14, 12, '2023-02-09 17:17:49.45026', 11,'2023-07-13 17:17:49.45023'),
    (12,'Test Description5', 11, 13, 12, 15, 12, 11, 11, '2023-07-09 17:17:49.45025', 11,'2023-07-14 17:17:49.45023'),
    (12,'Test Description5', 11, 13, 12, 16, 12, 11, 11, '2023-07-09 17:17:49.45025', 11,'2023-07-14 17:17:49.45023'),
    (12,'Test Description5', 11, 13, 12, 17, 16, 11, 11, '2023-07-09 17:17:49.45025', 11,'2023-07-14 17:17:49.45023'),
    (12,'Test Description5', 11, 13, 12, 18, 16, 11, 11, '2023-07-09 17:17:49.45025', 11,'2023-07-14 17:17:49.45023'),
    (12,'Test Description5', 11, 13, 12, 19, 16, 11, 11, '2023-07-09 17:17:49.45025', 11,'2023-07-14 17:17:49.45023'),
    (12,'Test Description5', 11, 13, 12, 20, 12, 11, 11, '2023-07-09 17:17:49.45025', 11,'2023-07-14 17:17:49.45023'),
    (12,'Test Description5', 11, 13, 12, 21, 12, 11, 11, '2023-07-09 17:17:49.45025', 11,'2023-07-14 17:17:49.45023'),
    (12,'Test Description5', 11, 13, 12, 22, 12, 11, 11, '2023-07-09 17:17:49.45025', 11,'2023-07-14 17:17:49.45023'),
    (12,'Test Description5', 11, 13, 12, 23, 16, 11, 11, '2023-07-09 17:17:49.45025', 11,'2023-07-14 17:17:49.45023'),
    (12,'Test Description5', 11, 13, 12, 24, 12, 11, 11, '2023-07-09 17:17:49.45025', 11,'2023-07-14 17:17:49.45023'),
    (12,'Test Description5', 11, 13, 12, 25, 14, 11, 11, '2023-07-09 17:17:49.45025', 11,'2023-07-14 17:17:49.45023'),
    (12,'Test Description5', 11, 13, 12, 26, 14, 11, 11, '2023-07-09 17:17:49.45025', 11,'2023-07-14 17:17:49.45023'),
    (12,'Test Description6', 12, 13, 13, 27, 13, 12, 12, '2023-07-01 17:17:49.45028', 11,'2023-07-15 17:17:49.45023');

INSERT INTO DEFECT_COMMENT (CREATED_BY_ID, DEFECT_ID, ID, CREATED_AT, CONTENT)
VALUES
    (11, 11, 11, '2023-07-09 17:17:49.450168', 'Test Comment1'),
    (12, 11, 12, '2023-07-09 17:17:49.450168', 'Test Comment2'),
    (11, 12, 13, '2023-07-09 17:17:49.450168', 'Test Comment3'),
    (11, 13, 14, '2023-07-09 17:17:49.450168', 'Test Comment4'),
    (11, 14, 15, '2023-07-09 17:17:49.450168', 'Test Comment5'),
    (11, 14, 16, '2023-07-09 17:17:49.450168', 'Test Comment6'),
    (11, 14, 17, '2023-07-09 17:17:49.450168', 'Test Comment7'),
    (11, 14, 18, '2023-07-09 17:17:49.450168', 'Test Comment8'),
    (11, 14, 19, '2023-07-09 17:17:49.450168', 'Test Comment9'),
    (11, 14, 20, '2023-07-09 17:17:49.450168', 'Test Comment10'),
    (11, 14, 21, '2023-07-09 17:17:49.450168', 'Test Comment11'),
    (11, 14, 22, '2023-07-09 17:17:49.450168', 'Test Comment12'),
    (11, 14, 23, '2023-07-09 17:17:49.450168', 'Test Comment13'),
    (11, 14, 24, '2023-07-09 17:17:49.450168', 'Test Comment14'),
    (11, 14, 25, '2023-07-09 17:17:49.450168', 'Test Comment15'),
    (11, 14, 26, '2023-07-09 17:17:49.450168', 'Test Comment16'),
    (11, 14, 27, '2023-07-09 17:17:49.450168', 'Test Comment17'),
    (11, 14, 28, '2023-07-09 17:17:49.450168', 'Test Comment18'),
    (11, 14, 29, '2023-07-09 17:17:49.450168', 'Test Comment19'),
    (11, 14, 30, '2023-07-09 17:17:49.450168', 'Test Comment20'),
    (11, 14, 31, '2023-07-09 17:17:49.450168', 'Test Comment21'),
    (11, 14, 32, '2023-07-09 17:17:49.450168', 'Test Comment22'),
    (11, 14, 33, '2023-07-09 17:17:49.450168', 'Test Comment23'),
    (12, 15, 34, '2023-07-09 17:17:49.450168', 'Test Comment24');

INSERT INTO DEFECT_IMAGE (DEFECT_ID, ID, UUID)
VALUES
    (11, 11, 'we41fwe564f1645wefwe6531f65we1fwe1f32q1d3qw51dq6wdq'),
    (11, 12, '14w6ef65we65f4wegf465w64rweg65wef3515wef16we5f16we'),
    (12, 13, '31wef51we6f51we65f1w65eq654w1f65we1f651we65f16f1wef6'),
    (13, 14, '1w6f156wef651wef651we65f1we6q554g65reg4165we16g16e'),
    (14, 15, '64t4h1rt5nj1gwk61z65tz1jh3r1tkth2r6gs4fa15665wgfa'),
    (14, 16, '46jhzq6r5w4efrek496e4htgf1DFT4J1+HR15TG30f6f1rg16'),
    (15, 17, 'b2er6g654wef464ew4fg6h1tw325g14fe6gt1hwqrf321bgh6rz5'),
    (16, 18, '456fewq465qwef6ewfwe6ffrqwr5f1we1f3wefew156e4e412rheh');

INSERT INTO ACTION (CREATED_BY_ID, DEFECT_ID, DUE_DATE, ID, IS_COMPLETED, CREATED_AT, DESCRIPTION, CHANGED_BY_ID, CHANGED_AT)
VALUES
    (11, 11, '2023-07-01', 11, FALSE, '2023-07-09 17:17:49.41829', 'Test Action 1', 11, '2023-07-09 17:17:49.41829'),
    (12, 11, '2023-07-02', 12, TRUE, '2023-07-09 17:17:49.41829', 'Test Action 2',  11, '2023-07-09 17:17:49.41829'),
    (11, 12, '2023-07-03', 13, FALSE, '2023-07-09 17:17:49.41829', 'Test Action 3', 11, '2023-07-09 17:17:49.41829'),
    (12, 12, '2023-07-04', 14, TRUE, '2023-07-09 17:17:49.41829', 'Test Action 4',  11, '2023-07-09 17:17:49.41829'),
    (11, 12, '2023-07-05', 15, FALSE, '2023-07-09 17:17:49.41829', 'Test Action 5', 11, '2023-07-09 17:17:49.41829'),
    (12, 13, '2023-07-06', 16, TRUE, '2023-07-09 17:17:49.41829', 'Test Action 6', 11, '2023-07-09 17:17:49.41829'),
    (11, 14, '2023-07-07', 17, FALSE, '2023-07-09 17:17:49.41829', 'Test Action 7', 11, '2023-07-09 17:17:49.41829'),
    (12, 15, '2023-07-08', 18, TRUE, '2023-07-09 17:17:49.41829', 'Test Action 8', 11, '2023-07-09 17:17:49.41829'),
    (11, 16, '2023-07-09', 19, FALSE, '2023-07-09 17:17:49.41829', 'Test Action 9', 11, '2023-07-09 17:17:49.41829'),
    (12, 16, '2023-07-10', 20, TRUE, '2023-07-09 17:17:49.41829', 'Test Action 10', 11, '2023-07-09 17:17:49.41829');

INSERT INTO USER_ACTIONS (ACTION_ID, USER_ID)
VALUES
    (11, 11),
    (12, 12),
    (13, 11),
    (14, 12),
    (15, 11),
    (16, 12),
    (17, 11),
    (18, 12),
    (19, 11),
    (20, 12);

