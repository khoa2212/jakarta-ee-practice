package com.axonactive.dojo.assignment.rest;

import com.axonactive.dojo.assignment.dto.*;
import com.axonactive.dojo.assignment.service.AssignmentService;
import com.axonactive.dojo.base.exception.BadRequestException;
import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.base.filter.Secure;
import com.axonactive.dojo.base.message.DeleteSuccessMessage;
import com.axonactive.dojo.base.message.LoggerMessage;
import com.axonactive.dojo.department.dto.DepartmentDTO;
import com.axonactive.dojo.department.dto.DepartmentListResponseDTO;
import com.axonactive.dojo.employee.rest.EmployeeResource;
import io.swagger.annotations.*;
import lombok.Getter;
import lombok.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("assignments")
@Api(tags = "Assignments API")
public class AssignmentResource {

    private static final Logger logger = LogManager.getLogger(AssignmentResource.class);

    @Inject
    private AssignmentService assignmentService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Get all assignment list with pagination")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Get all assignment list successfully", response = AssignmentListResponseDTO.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    public Response findAssignments(@DefaultValue("1") @QueryParam("projectId") long projectId,
                                    @DefaultValue("0") @QueryParam("employeeId") long employeeId,
                                    @DefaultValue("1") @QueryParam("pageNumber") int pageNumber,
                                    @DefaultValue("10") @QueryParam("pageSize") int pageSize) throws EntityNotFoundException {
        logger.info(LoggerMessage.findPaginatedListMessage("assignment"));

        AssignmentListResponseDTO assignmentListResponseDTO = this.assignmentService.findAssignments(projectId, employeeId, pageNumber, pageSize);
        return Response.ok().entity(assignmentListResponseDTO).build();
    }

    @POST
    @Path("add")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Create new assignment")
    @ApiModelProperty
    @ApiResponses({
            @ApiResponse(code = 201, message = "Create assignment successfully", response = AssignmentDTO.class),
            @ApiResponse(code = 400, message = "Request sent to the server is invalid"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    @Secure
    @RolesAllowed({"ADMIN", "USER"})
    public Response add(@Valid CreateAssignmentRequestDTO requestDTO) throws BadRequestException, EntityNotFoundException {
        logger.info(LoggerMessage.addMessage(requestDTO.toString()));

        AssignmentDTO assignmentDTO = this.assignmentService.add(requestDTO);

        String path = String.format("%s/%d", uriInfo.getAbsolutePath().getPath(), assignmentDTO.getId());

        return Response.created(URI.create(path)).entity(assignmentDTO).build();
    }

    @PUT
    @Path("update")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Update assignment")
    @ApiModelProperty
    @ApiResponses({
            @ApiResponse(code = 201, message = "Update assignment successfully", response = AssignmentDTO.class),
            @ApiResponse(code = 400, message = "Request sent to the server is invalid"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    @Secure
    @RolesAllowed({"ADMIN", "USER"})
    public Response update(@Valid UpdateAssignmentRequestDTO requestDTO) throws BadRequestException, EntityNotFoundException {
        logger.info(LoggerMessage.updateMessage(requestDTO.toString()));

        AssignmentDTO assignmentDTO = this.assignmentService.update(requestDTO);
        String path = String.format("%s/%d", uriInfo.getAbsolutePath().getPath(), assignmentDTO.getId());

        return Response.created(URI.create(path)).entity(assignmentDTO).build();
    }

    @DELETE
    @Path("delete")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Delete assignment")
    @ApiModelProperty
    @ApiResponses({
            @ApiResponse(code = 204, message = "Delete assignment successfully"),
            @ApiResponse(code = 400, message = "Request sent to the server is invalid"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    @Secure
    @RolesAllowed({"ADMIN"})
    public Response delete(@Valid DeleteAssignmentRequestDTO requestDTO) throws EntityNotFoundException {
        logger.info(LoggerMessage.deleteMessage(requestDTO.toString()));

        DeleteSuccessMessage result = this.assignmentService.delete(requestDTO);
        return Response.noContent().build();
    }
}
