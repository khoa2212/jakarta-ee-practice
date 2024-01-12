package org.eclipse.jakarta.department.service;

import org.eclipse.jakarta.department.entity.Department;

import javax.inject.Inject;
import java.util.List;

public class DepartmentService {

    @Inject
    DepartmentService departmentService;

    public void createDepartment(Department newDepartment) {
        departmentService.createDepartment(newDepartment);
    }

    public List<Department> getDepartments() {
        return departmentService.getDepartments();
    }
}
