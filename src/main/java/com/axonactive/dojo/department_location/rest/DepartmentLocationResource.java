package com.axonactive.dojo.department_location.rest;

import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.department_location.dto.DepartmentLocationDTO;
import com.axonactive.dojo.department_location.dto.DepartmentLocationListResponseDTO;
import com.axonactive.dojo.department_location.service.DepartmentLocationService;

import javax.inject.Inject;
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
        DepartmentLocationListResponseDTO departmentLocationListResponseDTO = this.departmentLocationService.findDepartmentLocationByDepartmentId(departmentId, pageNumber, pageSize);
        return Response.ok().entity(departmentLocationListResponseDTO).build();
    }
}
