CREATE TABLE location (
                          id SERIAL PRIMARY KEY,
                          custom_id TEXT UNIQUE,
                          name TEXT UNIQUE
);

CREATE TABLE role (
                      role_id SERIAL PRIMARY KEY,
                      name TEXT NOT NULL
);

CREATE TABLE "users" (
                        id SERIAL PRIMARY KEY,
                        custom_id TEXT UNIQUE,
                        username TEXT NOT NULL UNIQUE,
                        first_name TEXT,
                        last_name TEXT,
                        created_at TIMESTAMP,
                        changed_at TIMESTAMP,
                        created_by INT,
                        changed_by INT,
                        is_active BOOLEAN NOT NULL,
                        password TEXT NOT NULL,
                        mail TEXT NOT NULL UNIQUE,
                        location_id INT,

                        FOREIGN KEY (location_id) REFERENCES location(id)
);

CREATE TABLE user_roles (
                            user_id INT NOT NULL,
                            role_id INT NOT NULL,

                            PRIMARY KEY (user_id, role_id),

                            FOREIGN KEY (user_id) REFERENCES "users"(id),
                            FOREIGN KEY (role_id) REFERENCES role(role_id)
);

CREATE TABLE defect_status (
                               id SERIAL PRIMARY KEY,
                               name TEXT UNIQUE
);

CREATE TABLE defect_type (
                             id SERIAL PRIMARY KEY,
                             name TEXT UNIQUE
);

CREATE TABLE process (
                         id SERIAL PRIMARY KEY,
                         name TEXT UNIQUE
);

CREATE TABLE material (
                          id SERIAL PRIMARY KEY,
                          custom_id TEXT UNIQUE,
                          name TEXT UNIQUE
);

CREATE TABLE supplier (
                          id SERIAL PRIMARY KEY,
                          custom_id TEXT UNIQUE,
                          name TEXT UNIQUE
);

CREATE TABLE lot (
                     id SERIAL PRIMARY KEY,
                     lot_number TEXT UNIQUE,
                     material_id INT,
                     supplier_id INT,

                     FOREIGN KEY (material_id) REFERENCES material(id),
                     FOREIGN KEY (supplier_id) REFERENCES supplier(id)
);

CREATE TABLE defect (
                        id SERIAL PRIMARY KEY,
                        description TEXT,
                        changed_at TIMESTAMP,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        defect_status_id INT NOT NULL,
                        lot_id INT NOT NULL,
                        location_id INT NOT NULL,
                        process_id INT NOT NULL,
                        defect_type_id INT NOT NULL,
                        created_by_id INT NOT NULL,
                        changed_by_id INT,

                        FOREIGN KEY (defect_status_id) REFERENCES defect_status(id),
                        FOREIGN KEY (lot_id) REFERENCES lot(id),
                        FOREIGN KEY (location_id) REFERENCES location(id),
                        FOREIGN KEY (process_id) REFERENCES process(id),
                        FOREIGN KEY (defect_type_id) REFERENCES defect_type(id),
                        FOREIGN KEY (created_by_id) REFERENCES "users"(id),
                        FOREIGN KEY (changed_by_id) REFERENCES "users"(id)
);

CREATE TABLE defect_comment (
                                id SERIAL PRIMARY KEY,
                                content TEXT,
                                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                created_by_id INT,
                                defect_id INT NOT NULL,

                                FOREIGN KEY (defect_id) REFERENCES defect(id),
                                FOREIGN KEY (created_by_id) REFERENCES "users"(id)
);

CREATE TABLE defect_image (
                              id SERIAL PRIMARY KEY,
                              path TEXT,
                              defect_id INT
                                  NOT NULL,

                              FOREIGN KEY (defect_id) REFERENCES defect(id)
);

CREATE TABLE action (
        id SERIAL PRIMARY KEY,
        description TEXT,
        due_date DATE NOT NULL,
        is_completed BOOLEAN NOT NULL,
        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        changed_at TIMESTAMP,
        defect_id INT,
        created_by_id INT NOT NULL,
        changed_by_id INT,
        FOREIGN KEY (defect_id) REFERENCES defect(id),
        FOREIGN KEY (created_by_id) REFERENCES "users"(id),
        FOREIGN KEY (changed_by_id) REFERENCES "users"(id)
);


CREATE TABLE user_actions (
                             user_id INT,
                             action_id INT,
                             PRIMARY KEY (user_id, action_id),
                             FOREIGN KEY (user_id) REFERENCES "users"(id),
                             FOREIGN KEY (action_id) REFERENCES action(id)
);




