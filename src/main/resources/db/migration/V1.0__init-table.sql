CREATE TABLE department (
    id                      SERIAL PRIMARY KEY,
    start_date              TIMESTAMP,
    department_name         VARCHAR(200),
    created_at              TIMESTAMP,
    updated_at              TIMESTAMP
);

CREATE TABLE department_location (
    id                      SERIAL PRIMARY KEY,
    location                VARCHAR(100),
    department_id           SERIAL,
    created_at              TIMESTAMP,
    updated_at              TIMESTAMP
);

CREATE TABLE employee (
    id                      SERIAL PRIMARY KEY,
    first_name              VARCHAR(20),
    middle_name             VARCHAR(20),
    last_name               VARCHAR(20),
    salary                  DOUBLE PRECISION,
    gender                  VARCHAR(10),
    date_of_birth           TIMESTAMP,
    department_id           SERIAL,
    created_at              TIMESTAMP,
    updated_at              TIMESTAMP
);

CREATE TABLE project (
    id                      SERIAL PRIMARY KEY,
    project_name            VARCHAR(100),
    area                    VARCHAR(100),
    department_id           SERIAL,
    created_at              TIMESTAMP,
    updated_at              TIMESTAMP
);

CREATE TABLE assignment (
    id                      SERIAL PRIMARY KEY,
    number_of_hour          INTEGER,
    employee_id             SERIAL,
    project_id              SERIAL,
    created_at              TIMESTAMP,
    updated_at              TIMESTAMP
);

CREATE TABLE relatives (
    id                      SERIAL PRIMARY KEY,
    full_name                VARCHAR(100),
    gender                  VARCHAR(10),
    phone_number            VARCHAR(15),
    relationship            VARCHAR(100),
    employee_id             SERIAL,
    created_at              TIMESTAMP,
    updated_at              TIMESTAMP
);
