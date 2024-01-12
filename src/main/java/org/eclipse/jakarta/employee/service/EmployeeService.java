package org.eclipse.jakarta.employee.service;

import org.eclipse.jakarta.employee.entity.Employee;
import org.eclipse.jakarta.employee.repository.EmployeeRepository;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Stateless
public class EmployeeService {

    @Inject
    EmployeeRepository employeeRepository;

    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    public void createEmployee(Employee newEmp) {
        employeeRepository.create(newEmp);
    }
}
