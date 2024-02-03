CREATE TABLE department (
    id                      SERIAL PRIMARY KEY,
    start_date              TIMESTAMP,
    department_name          VARCHAR(100),
    created_at              TIMESTAMP,
    updated_at              TIMESTAMP
);