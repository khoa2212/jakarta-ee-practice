package com.axonactive.dojo.employee.rest;

import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.employee.dto.*;
import com.axonactive.dojo.employee.service.EmployeeService;

import javax.inject.Inject;
import javax.json.JsonObject;
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
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findById(@PathParam("id") Long id) throws EntityNotFoundException {
        EmployeeDTO employeeDTO = this.employeeService.findById(id);
        return Response.ok().entity(employeeDTO).build();
    }

    @POST
    @Path("add")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response add(@Valid CreateEmployeeRequestDTO reqDTO) throws EntityNotFoundException {
        EmployeeDTO employeeDTO = this.employeeService.add(reqDTO);
        return Response.ok(employeeDTO).build();
    }

    @PUT
    @Path("update")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(@Valid UpdateEmployeeRequestDTO reqDTO) throws EntityNotFoundException {
        EmployeeDTO employeeDTO = this.employeeService.update(reqDTO);
        return Response.ok().entity(employeeDTO).build();
    }

    @DELETE
    @Path("delete")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response delete(@Valid DeleteEmployeeRequestDTO reqDTO) throws EntityNotFoundException {
        JsonObject jobj = this.employeeService.deleteSoftly(reqDTO);
        return Response.ok().entity(jobj).build();
    }

}
