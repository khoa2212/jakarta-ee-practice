package com.axonactive.dojo.department.service;

import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.department.dao.DepartmentDAO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Stateless
public class DepartmentService {
    @Inject
    DepartmentDAO departmentDAO;

    public List<Department> getDepartments() {
        System.out.println("here");
        return departmentDAO.findAll();
    }
//
//    public Department createDepartment(Department newDep) {
//        System.out.println("here createDepartment");
//        return departmentDAO.create(newDep);
//    }

    public Optional<Department> getDeparmentById (Long id) {
        return this.departmentDAO.findById(id);
    }
}
