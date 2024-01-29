package com.axonactive.dojo.employee.service;

import com.axonactive.dojo.assignment.dao.AssignmentDao;
import com.axonactive.dojo.base.exception.BadRequestException;
import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.employee.dto.AddEmployeeRequestDTO;
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

    public List<Employee> get(Long dpt) throws EntityNotFoundException {
        throw new EntityNotFoundException("Employee Not Found.");
//        throw new BadRequestException("Bad request");

//        return employeeDAO.getEmployeeByDepartmentID(dpt);
    }

    public void createEmployeeFromDto(AddEmployeeRequestDTO newEmp) {
        Employee employee = new Employee();
        employee.setFirstName(newEmp.getFirstName());
        employee.setLastName(newEmp.getLastName());
        employeeDAO.add(employee);
    }
}
