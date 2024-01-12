package org.eclipse.jakarta.employee.controller;

import org.eclipse.jakarta.employee.entity.Employee;
import org.eclipse.jakarta.employee.service.EmployeeService;
import org.eclipse.jakarta.hello.Hello;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("emp")
public class EmployeeController {
    @Inject
    EmployeeService employeeService;

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public List<Employee> getEmployees () {
        return employeeService.getEmployees();
    }
}
