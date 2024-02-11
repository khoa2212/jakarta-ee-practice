package com.axonactive.dojo.department.rest;

import com.axonactive.dojo.department.dto.CreateDepartmentRequestDTO;
import com.axonactive.dojo.department.dto.DepartmentDTO;
import com.axonactive.dojo.department.dto.DepartmentListResponseDTO;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.department.service.DepartmentService;

import javax.inject.Inject;
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
    public Response getDepartments(@DefaultValue("1") @QueryParam("pageNumber") Integer pageNumber,
                                           @DefaultValue("10") @QueryParam("pageSize") Integer pageSize) {
        DepartmentListResponseDTO departmentListResponseDTO = this.departmentService.findDepartments(pageNumber, pageSize);
        return Response.ok().entity(departmentListResponseDTO).build();
    }

    @POST
    @Path("create")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createDepartments(@Valid CreateDepartmentRequestDTO createDepartmentRequestDTO) {
        DepartmentDTO departmentDTO = this.departmentService.add(createDepartmentRequestDTO);
        return Response.ok().entity(departmentDTO).build();
    }
}
