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
    private DepartmentDAO departmentDAO;

    public List<Department> getDepartments() {
        return departmentDAO.findAll();
    }

    public Optional<Department> getDepartmentById (Long id) {
        return this.departmentDAO.findById(id);
    }
}
