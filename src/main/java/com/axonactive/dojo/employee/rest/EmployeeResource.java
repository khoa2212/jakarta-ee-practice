package com.axonactive.dojo.employee.rest;

import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.employee.dto.CreateEmployeeRequestDTO;
import com.axonactive.dojo.employee.dto.EmployeeDTO;
import com.axonactive.dojo.employee.dto.EmployeeListResponseDTO;
import com.axonactive.dojo.employee.service.EmployeeService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;


@Path("employees")
public class EmployeeResource {

    @Inject
    private EmployeeService employeeService;

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public Response findEmployees(@DefaultValue("1") @QueryParam("pageNumber") Integer pageNumber,
                                 @DefaultValue("10") @QueryParam("pageSize") Integer pageSize,
                                 @DefaultValue("0") @QueryParam("departmentId") Long departmentId) throws EntityNotFoundException {
        EmployeeListResponseDTO employeeListResponseDTO = this.employeeService.findEmployees(departmentId, pageNumber, pageSize);
        return Response.ok().entity(employeeListResponseDTO).build();
    }

    @POST
    @Path("add")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createEmp(@Valid CreateEmployeeRequestDTO reqDTO) throws EntityNotFoundException {
        EmployeeDTO employeeDTO = this.employeeService.add(reqDTO);
        return Response.ok(employeeDTO).build();
    }
}
