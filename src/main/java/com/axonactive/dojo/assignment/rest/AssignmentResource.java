package com.axonactive.dojo.assignment.rest;

import com.axonactive.dojo.assignment.dto.AssignmentListResponseDTO;
import com.axonactive.dojo.assignment.service.AssignmentService;
import com.axonactive.dojo.base.exception.EntityNotFoundException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("assignments")
public class AssignmentResource {

    @Inject
    private AssignmentService assignmentService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAssignments(@DefaultValue("1") @QueryParam("projectId") Long projectId,
                                    @DefaultValue("0") @QueryParam("employeeId") Long employeeId,
                                    @DefaultValue("1") @QueryParam("pageNumber") Integer pageNumber,
                                    @DefaultValue("10") @QueryParam("pageSize") Integer pageSize) throws EntityNotFoundException {
        AssignmentListResponseDTO assignmentListResponseDTO = this.assignmentService.findAssignments(projectId, employeeId, pageNumber, pageSize);
        return Response.ok().entity(assignmentListResponseDTO).build();
    }
}
