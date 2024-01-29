package com.axonactive.dojo.employee.rest;

import com.axonactive.dojo.base.exception.AppExceptionMapper;
import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.base.exception.InternalException;
import com.axonactive.dojo.base.exception.UnauthorizedException;
import com.axonactive.dojo.employee.dto.AddEmployeeRequestDTO;
import com.axonactive.dojo.employee.service.EmployeeService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;


@Path("employees")
public class EmployeeResource {

    @Context
    private HttpHeaders headers;

    @Inject
    EmployeeService employeeService;

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public Response getEmployeesByDepartmentID(@QueryParam("departmentID") Long id)  {
        try {
            return Response.ok(employeeService.get(id)).build();
        } catch (EntityNotFoundException e) {
            return new AppExceptionMapper().toResponse(e);
        }
    }


//    @GET
//    @Produces({ MediaType.APPLICATION_JSON })
//    public List<Employee> getEmployees() {
//        return employeeService.getEmployees();
//    }

    @POST
    @Path("create")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createEmp(@Valid AddEmployeeRequestDTO newEmp) throws UnauthorizedException {

        MultivaluedMap<String, String> requestHeaders = headers.getRequestHeaders();
        String authorizationHeader = requestHeaders.getFirst("Authorization");

        if (authorizationHeader == null) {
            throw new UnauthorizedException("Unauthorized");
        }

        employeeService.createEmployeeFromDto(newEmp);
        return Response.ok(newEmp).build();
    }

    @GET
    @Path("dummy")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response dummy() throws InternalException {
        throw new InternalException("Internal server error");
    }
}
