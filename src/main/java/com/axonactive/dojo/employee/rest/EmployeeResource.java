package com.axonactive.dojo.employee.rest;

import com.axonactive.dojo.employee.entity.Employee;
import com.axonactive.dojo.employee.service.EmployeeService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("employees")
public class EmployeeResource {
    @Inject
    EmployeeService employeeService;

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public Response getEmployeesByDepartmentID(@QueryParam("departmentID") Long id) { return Response.ok(employeeService.get(id)).build(); }


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

        return Response.ok(newEmp).build();
    }
}
