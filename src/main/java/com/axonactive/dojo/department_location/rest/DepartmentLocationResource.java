package com.axonactive.dojo.department_location.rest;

import com.axonactive.dojo.base.exception.BadRequestException;
import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.department_location.dto.*;
import com.axonactive.dojo.department_location.service.DepartmentLocationService;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("department-locations")
public class DepartmentLocationResource {


    @Inject
    private DepartmentLocationService departmentLocationService;

    @GET
    @Path("department/{departmentId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findDepartmentLocationByDepartmentId(@PathParam("departmentId") Long departmentId,
                                                         @DefaultValue("1") @QueryParam("pageNumber") Integer pageNumber,
                                                         @DefaultValue("10") @QueryParam("pageSize") Integer pageSize) throws EntityNotFoundException {
        DepartmentLocationListResponseDTO departmentLocationListResponseDTO = this.departmentLocationService.findDepartmentsLocationByDepartmentId(departmentId, pageNumber, pageSize);
        return Response.ok().entity(departmentLocationListResponseDTO).build();
    }

    @POST
    @Path("add")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response add(@Valid CreateDepartmentLocationRequestDTO createDepartmentLocationRequestDTO) throws BadRequestException, EntityNotFoundException {
        DepartmentLocationDTO departmentLocationDTO = this.departmentLocationService.add(createDepartmentLocationRequestDTO);
        return Response.ok().entity(departmentLocationDTO).build();
    }

    @PUT
    @Path("update")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(@Valid UpdateDepartmentLocationRequestDTO updateDepartmentLocationRequestDTO) throws BadRequestException, EntityNotFoundException {
        DepartmentLocationDTO departmentLocationDTO = this.departmentLocationService.update(updateDepartmentLocationRequestDTO);
        return Response.ok().entity(departmentLocationDTO).build();
    }

    @DELETE
    @Path("delete")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response delete(@Valid DeleteDepartmentLocationRequestDTO deleteDepartmentLocationRequestDTO) throws EntityNotFoundException {
        JsonObject jobj = this.departmentLocationService.delete(deleteDepartmentLocationRequestDTO);
        return Response.ok().entity(jobj).build();
    }
}
