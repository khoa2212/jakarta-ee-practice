package com.axonactive.dojo.department.rest;

import com.axonactive.dojo.base.exception.BadRequestException;
import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.department.dto.*;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.department.service.DepartmentService;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("departments")
public class DepartmentResource {

    @Inject
    DepartmentService departmentService;

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public Response findDepartments(@DefaultValue("1") @QueryParam("pageNumber") Integer pageNumber,
                                           @DefaultValue("10") @QueryParam("pageSize") Integer pageSize) {
        DepartmentListResponseDTO departmentListResponseDTO = this.departmentService.findDepartments(pageNumber, pageSize);
        return Response.ok().entity(departmentListResponseDTO).build();
    }

    @GET
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response findById(@PathParam("id") Long id) throws EntityNotFoundException {
        DepartmentDTO departmentDTO = this.departmentService.findById(id);
        return Response.ok().entity(departmentDTO).build();
    }

    @POST
    @Path("add")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response add(@Valid CreateDepartmentRequestDTO createDepartmentRequestDTO) throws BadRequestException {
        DepartmentDTO departmentDTO = this.departmentService.add(createDepartmentRequestDTO);
        return Response.ok().entity(departmentDTO).build();
    }

    @PUT
    @Path("update")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(@Valid UpdateDepartmentRequestDTO updateDepartmentRequestDTO) throws EntityNotFoundException, BadRequestException {
        DepartmentDTO departmentDTO = this.departmentService.update(updateDepartmentRequestDTO);
        return Response.ok().entity(departmentDTO).build();
    }

    @DELETE
    @Path("delete")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(@Valid DeleteDepartmentRequestDTO deleteDepartmentRequestDTO) throws EntityNotFoundException, BadRequestException {
        JsonObject jobj = this.departmentService.deleteSoftly(deleteDepartmentRequestDTO);
        return Response.ok().entity(jobj).build();
    }
}
