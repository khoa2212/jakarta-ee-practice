package org.eclipse.jakarta.department.service;

import org.eclipse.jakarta.department.entity.Department;
import org.eclipse.jakarta.department.repository.DepartmentRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class DepartmentService {

    @Inject
    DepartmentRepository departmentRepository;

    public void createDepartment(Department newDepartment) {
        departmentRepository.create(newDepartment);
    }

    public List<Department> getDepartments() {
        return departmentRepository.findAll();
    }
}
