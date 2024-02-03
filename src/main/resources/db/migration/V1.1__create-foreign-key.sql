ALTER TABLE department_location
ADD
    CONSTRAINT fk_department_location_department
    FOREIGN KEY (department_id) REFERENCES department(id);

ALTER TABLE employee
ADD
    CONSTRAINT fk_employee_department
    FOREIGN KEY (department_id) REFERENCES department(id);

ALTER TABLE project
ADD
    CONSTRAINT fk_project_department
    FOREIGN KEY (department_id) REFERENCES department(id);

ALTER TABLE relatives
ADD
    CONSTRAINT fk_relatives_employee
    FOREIGN KEY (employee_id) REFERENCES employee(id);

ALTER TABLE assignment
ADD
    CONSTRAINT fk_assignment_employee
    FOREIGN KEY (employee_id) REFERENCES employee(id),
ADD
    CONSTRAINT fk_assignment_project
    FOREIGN KEY (project_id) REFERENCES project(id);

ALTER TABLE assignment
ADD
    CONSTRAINT UQ_employee_id_project_id
    UNIQUE(employee_id, project_id);