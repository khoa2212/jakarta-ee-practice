package com.axonactive.dojo.assignment.rest;

import com.axonactive.dojo.assignment.dto.AssignmentDTO;
import com.axonactive.dojo.assignment.dto.AssignmentListResponseDTO;
import com.axonactive.dojo.assignment.dto.CreateAssignmentRequestDTO;
import com.axonactive.dojo.assignment.service.AssignmentService;
import com.axonactive.dojo.base.exception.BadRequestException;
import com.axonactive.dojo.base.exception.EntityNotFoundException;
import lombok.Getter;
import lombok.Value;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

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

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response add(@Valid CreateAssignmentRequestDTO requestDTO) throws BadRequestException, EntityNotFoundException {
        AssignmentDTO assignmentDTO = this.assignmentService.add(requestDTO);
        return Response.ok().entity(assignmentDTO).build();
    }

//    @GET
//    @Path("project/{projectId}")
//    @Produces({MediaType.APPLICATION_JSON})
//    public Response findAllAssignmentsByProjectId(@PathParam("projectId") Long projectId) throws EntityNotFoundException {
//        List<AssignmentDTO> assignmentDTOS = this.assignmentService.findAllAssignmentsByProjectId(projectId);
//        return Response.ok().entity(assignmentDTOS).build();
//    }



}
