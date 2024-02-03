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
    DepartmentDAO departmentService;

    @Inject
    AssignmentDao assignmentDAO;

    public List<Employee> getEmployees() {
        return employeeDAO.findAll();
    }

    public Employee add(AddEmployeeRequestDTO reqDTO) throws EntityNotFoundException {
        var department = this.departmentService.findById(reqDTO.getDepartmentId());

        if(!department.isPresent()) {
            throw new EntityNotFoundException("Department not found");
        }
        var newEmp = Employee
                .builder()
                .firstName(reqDTO.getFirstName())
                .middleName(reqDTO.getMiddleName())
                .lastName(reqDTO.getLastName())
                .dateOfBirth(reqDTO.getDateOfBirth())
                .gender(reqDTO.getGender())
                .salary(reqDTO.getSalary())
                .build();

        employeeDAO.add(newEmp);

        return newEmp;
    }
}
