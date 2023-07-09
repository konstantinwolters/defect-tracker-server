INSERT INTO ROLE (ROLE_ID, NAME) VALUES (1, 'ROLE_USER'), (2, 'ROLE_ADMIN');

INSERT INTO LOCATION (ID, NAME) VALUES (1, 'Texas'), (2, 'San Jose'), (3, 'Duesseldorf');

INSERT INTO USERS (ID, LOCATION_ID, FIRST_NAME, LAST_NAME, MAIL, PASSWORD, USERNAME)
VALUES (1, 1, null, null, 'bill@test.de', '$2a$10$UfUHZsNiorgvWlLig3y.DODxo0bRyGPCSptgkKgzW3dDDlIUkb5a6', 'bill'), (2, 1, null, null, 'bob@test.de', '$2a$10$UfUHZsNiorgvWlLig3y.DODxo0bRyGPCSptgkKgzW3dDDlIUkb5a6', 'bob');

INSERT INTO USER_ROLES (ROLE_ID, USER_ID) VALUES (2, 1), (1, 2);

INSERT INTO DEFECT_STATUS (ID, NAME) VALUES (1, 'New'), (2, 'QA check'), (3, 'Disposed'), (4, 'Cleared');

INSERT INTO DEFECT_TYPE (ID, NAME) VALUES (1, 'Foreign Body'), (2, 'Pest'), (3, 'Quality Issue');

INSERT INTO PROCESS (ID, NAME) VALUES (1, 'Test Process'), (2, 'Test Process 2');

INSERT INTO MATERIAL (ID, NAME) VALUES (1, 'Chocolate'), (2, 'Beer');

INSERT INTO SUPPLIER (ID, NAME) VALUES (1, 'Lindt'), (2, 'Oettinger');

INSERT INTO LOT (ID, MATERIAL_ID, SUPPLIER_ID, LOT_NUMBER)
VALUES (1, 1, 1, 'x0815x'), (2, 2, 2, '0816'), (3, 2, 2, '0817'), (4, 1, 1, '0818');

INSERT INTO DEFECT (CREATED_BY_ID, DEFECT_STATUS_ID, DEFECT_TYPE_ID, ID, LOCATION_ID, LOT_ID, PROCESS_ID, CREATED_ON)
VALUES (1, 1, 1, 1, 1, 1, 1, '2023-07-09 17:17:49.45023'), (1, 2, 2, 2, 2, 2, 2, '2023-04-09 17:17:49.45024'), (1, 3, 3, 3, 3, 3, 1, '2023-01-09 17:17:49.45025'), (2, 4, 1, 4, 1, 4, 2, '2023-02-09 17:17:49.45026'), (2, 1, 2, 5, 2, 1, 1, '2023-07-09 17:17:49.45025'), (2, 2, 3, 6, 3, 2, 2, '2023-07-01 17:17:49.45028');

INSERT INTO DEFECT_COMMENT (CREATED_BY_ID, DEFECT_ID, ID, CREATED_ON, CONTENT)
VALUES (1, 1, 1, '2023-07-09 17:17:49.450168', 'Test Comment1'), (2, 1, 2, '2023-07-09 17:17:49.450168', 'Test Comment2'), (1, 2, 3, '2023-07-09 17:17:49.450168', 'Test Comment3'), (1, 3, 4, '2023-07-09 17:17:49.450168', 'Test Comment4'), (1, 4, 5, '2023-07-09 17:17:49.450168', 'Test Comment5'), (2, 5, 6, '2023-07-09 17:17:49.450168', 'Test Comment6');

INSERT INTO DEFECT_IMAGE (DEFECT_ID, ID, PATH)
VALUES (1, 1, 'testpath1'), (1, 2, 'testpath2'), (2, 3, 'testpath3'), (3, 4, 'testpath4'), (4, 5, 'testpath5'), (4, 6, 'testpath6'), (5, 7, 'testpath7'), (6, 8, 'testpath8');

INSERT INTO ACTION (CREATED_BY_ID, DEFECT_ID, DUE_DATE, ID, IS_COMPLETED, CREATED_ON, DESCRIPTION)
VALUES
    (1, 1, '2023-07-01', 1, FALSE, '2023-07-09 17:17:49.41829', 'Test Action 1'),
    (2, 1, '2023-07-02', 2, TRUE, '2023-07-09 17:17:49.41829', 'Test Action 2'),
    (1, 2, '2023-07-03', 3, FALSE, '2023-07-09 17:17:49.41829', 'Test Action 3'),
    (2, 2, '2023-07-04', 4, TRUE, '2023-07-09 17:17:49.41829', 'Test Action 4'),
    (1, 2, '2023-07-05', 5, FALSE, '2023-07-09 17:17:49.41829', 'Test Action 5'),
    (2, 3, '2023-07-06', 6, TRUE, '2023-07-09 17:17:49.41829', 'Test Action 6'),
    (1, 4, '2023-07-07', 7, FALSE, '2023-07-09 17:17:49.41829', 'Test Action 7'),
    (2, 5, '2023-07-08', 8, TRUE, '2023-07-09 17:17:49.41829', 'Test Action 8'),
    (1, 6, '2023-07-09', 9, FALSE, '2023-07-09 17:17:49.41829', 'Test Action 9'),
    (2, 6, '2023-07-10', 10, TRUE, '2023-07-09 17:17:49.41829', 'Test Action 10');

INSERT INTO USER_ACTIONS (ACTION_ID, USER_ID)
VALUES (1, 1), (2, 2), (3, 1), (4, 2), (5, 1), (6, 2), (7, 1), (8, 2), (9, 1), (10, 2);

