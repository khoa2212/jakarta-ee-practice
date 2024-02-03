package com.axonactive.dojo.employee.rest;

import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.employee.dto.AddEmployeeRequestDTO;
import com.axonactive.dojo.employee.service.EmployeeService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;


@Path("employees")
public class EmployeeResource {

    @Inject
    EmployeeService employeeService;

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public Response getEmployees() {
        return Response.ok().entity(employeeService.getEmployees()).build();
    }

    @POST
    @Path("create")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createEmp(@Valid AddEmployeeRequestDTO reqDTO) throws EntityNotFoundException {
        var result = employeeService.add(reqDTO);
        return Response.ok(result).build();
    }
}
