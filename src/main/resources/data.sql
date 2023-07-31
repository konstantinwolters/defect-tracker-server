INSERT INTO ROLE (ROLE_ID, NAME)
VALUES
    (11, 'ROLE_USER'),
    (12, 'ROLE_ADMIN'),
    (13, 'ROLE_PURCHASER');

INSERT INTO LOCATION (ID, NAME)
VALUES
    (11, 'Texas'),
    (12, 'San Jose'),
    (13, 'Duesseldorf');

INSERT INTO USERS (ID, LOCATION_ID, FIRST_NAME, LAST_NAME, MAIL, PASSWORD, USERNAME, IS_ACTIVE, CREATED_BY, CREATED_AT)
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
    (13, 'Quality Issue');

INSERT INTO PROCESS (ID, NAME)
VALUES
    (11, 'Test Process'),
    (12, 'Test Process 2');

INSERT INTO MATERIAL (ID, NAME)
VALUES
    (11, 'Chocolate'),
    (12, 'Beer');

INSERT INTO SUPPLIER (ID, NAME)
VALUES
    (11, 'Lindt'),
    (12, 'Oettinger');

INSERT INTO LOT (ID, MATERIAL_ID, SUPPLIER_ID, LOT_NUMBER)
VALUES
    (11, 11, 11, 'x0815x'),
    (12, 12, 12, '0816'),
    (13, 12, 12, '0817'),
    (14, 11, 11, '0818');

INSERT INTO DEFECT (CREATED_BY_ID, DESCRIPTION, DEFECT_STATUS_ID, DEFECT_TYPE_ID, ID, LOCATION_ID, LOT_ID, PROCESS_ID, CREATED_AT, CHANGED_BY_ID, CHANGED_AT)
VALUES
    (11,'Test Description1', 11, 11, 11, 11, 11, 11, '2023-07-09 17:17:49.45023', 11,'2023-07-10 17:17:49.45023'),
    (11,'Test Description2', 12, 12, 12, 12, 12, 12, '2023-04-09 17:17:49.45024', 11,'2023-07-11 17:17:49.45023'),
    (11,'Test Description3', 13, 13, 13, 13, 13, 11, '2023-01-09 17:17:49.45025', 11,'2023-07-12 17:17:49.45023'),
    (12,'Test Description4', 14, 11, 14, 11, 14, 12, '2023-02-09 17:17:49.45026', 11,'2023-07-13 17:17:49.45023'),
    (12,'Test Description5', 11, 12, 15, 12, 11, 11, '2023-07-09 17:17:49.45025', 11,'2023-07-14 17:17:49.45023'),
    (12,'Test Description6', 12, 13, 16, 13, 12, 12, '2023-07-01 17:17:49.45028', 11,'2023-07-15 17:17:49.45023');

INSERT INTO DEFECT_COMMENT (CREATED_BY_ID, DEFECT_ID, ID, CREATED_AT, CONTENT)
VALUES
    (11, 11, 11, '2023-07-09 17:17:49.450168', 'Test Comment1'),
    (12, 11, 12, '2023-07-09 17:17:49.450168', 'Test Comment2'),
    (11, 12, 13, '2023-07-09 17:17:49.450168', 'Test Comment3'),
    (11, 13, 14, '2023-07-09 17:17:49.450168', 'Test Comment4'),
    (11, 14, 15, '2023-07-09 17:17:49.450168', 'Test Comment5'),
    (12, 15, 16, '2023-07-09 17:17:49.450168', 'Test Comment6');

INSERT INTO DEFECT_IMAGE (DEFECT_ID, ID, PATH)
VALUES
    (11, 11, 'testpath1'),
    (11, 12, 'testpath2'),
    (12, 13, 'testpath3'),
    (13, 14, 'testpath4'),
    (14, 15, 'testpath5'),
    (14, 16, 'testpath6'),
    (15, 17, 'testpath7'),
    (16, 18, 'testpath8');

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

