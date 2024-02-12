package com.axonactive.dojo.relative.rest;

import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.relative.dto.RelativeListResponseDTO;
import com.axonactive.dojo.relative.service.RelativeService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("relatives")
public class RelativeResource {

    @Inject
    private RelativeService relativeService;

    @GET
    @Path("employee/{employeeId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findRelativesByEmployeeId(@PathParam("employeeId") Long employeeId,
                                              @DefaultValue("1") @QueryParam("pageNumber") Integer pageNumber,
                                              @DefaultValue("10") @QueryParam("pageSize") Integer pageSize) throws EntityNotFoundException {
        RelativeListResponseDTO relativeListResponseDTO = this.relativeService.findRelativesByEmployeeId(employeeId, pageNumber, pageSize);
        return Response.ok().entity(relativeListResponseDTO).build();
    }
}
