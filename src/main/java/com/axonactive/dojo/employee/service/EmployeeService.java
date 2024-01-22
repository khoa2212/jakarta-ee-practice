package com.axonactive.dojo.employee.service;

import com.axonactive.dojo.assignment.dao.AssignmentDao;
import com.axonactive.dojo.employee.entity.Employee;
import com.axonactive.dojo.employee.dao.EmployeeDAO;
import com.axonactive.dojo.department.dao.DepartmentDAO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class EmployeeService {

    @Inject
    EmployeeDAO employeeDAO;

    @Inject
    DepartmentDAO departmentDAO;

    @Inject
    AssignmentDao assignmentDAO;

    public List<Employee> getEmployees() {
        return employeeDAO.findAll();
    }

    public void createEmployee(Employee newEmp) {
//        var department = this.departmentRepository.findById(newEmp.getDepartmentId());
//
//        if(department.isPresent()) {
//            newEmp.setDepartment(department.get());
//            employeeRepository.create(newEmp);
//        }
    }

    public List<Employee> get(Long dpt) {

        return employeeDAO.getEmployeeByDepartmentID(dpt);
    }
}
