package org.eclipse.jakarta.department.service;

import org.eclipse.jakarta.department.entity.Department;
import org.eclipse.jakarta.department.repository.DepartmentRepository;
import org.eclipse.jakarta.employee.entity.Employee;
import org.eclipse.jakarta.employee.repository.EmployeeRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Stateless
public class DepartmentService {
    @Inject
    DepartmentRepository departmentRepository;

    public List<Department> getDepartments() {
        System.out.println("here");
        return departmentRepository.findAll();
    }

    public Department createDepartment(Department newDep) {
        System.out.println("here createDepartment");
        return departmentRepository.create(newDep);
    }

    public Optional<Department> getDeparmentById (Long id) {
        return this.departmentRepository.findById(id);
    }
}
