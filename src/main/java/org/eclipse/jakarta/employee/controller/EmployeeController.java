package org.eclipse.jakarta.employee.controller;

import org.eclipse.jakarta.employee.entity.Employee;
import org.eclipse.jakarta.employee.service.EmployeeService;
import org.eclipse.jakarta.hello.Hello;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

    @POST
    @Path("create")
    @Produces({MediaType.APPLICATION_JSON})
    public Response createEmp(Employee newEmp) {
        employeeService.createEmployee(newEmp);
        return Response.status(Response.Status.OK)
                .entity("Create employee successfully")
                .build();
    }
}
