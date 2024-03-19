package com.axonactive.dojo.relative.rest;

import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.base.filter.Secure;
import com.axonactive.dojo.base.message.LoggerMessage;
import com.axonactive.dojo.department.dto.DepartmentListResponseDTO;
import com.axonactive.dojo.employee.rest.EmployeeResource;
import com.axonactive.dojo.relative.dto.RelativeListResponseDTO;
import com.axonactive.dojo.relative.service.RelativeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("relatives")
@Api(tags = "Relatives API")
public class RelativeResource {

    private static final Logger logger = LogManager.getLogger(RelativeResource.class);

    @Inject
    private RelativeService relativeService;

    @GET
    @Path("employee/{employeeId}")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Get all relative list with pagination")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Get all relative list successfully", response = RelativeListResponseDTO.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    public Response findRelativesByEmployeeId(@PathParam("employeeId") long employeeId,
            @DefaultValue("1") @QueryParam("pageNumber") int pageNumber,
            @DefaultValue("10") @QueryParam("pageSize") int pageSize) throws EntityNotFoundException {
        logger.info(LoggerMessage.findPaginatedListMessage("relative"));

        RelativeListResponseDTO relativeListResponseDTO = this.relativeService.findRelativesByEmployeeId(employeeId,
                pageNumber, pageSize);
        return Response.ok().entity(relativeListResponseDTO).build();
    }

    @GET
    @Path("reports/employees")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Get all relative list by employees not assigned")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Get all relative list successfully", response = RelativeListResponseDTO.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    @Secure
    @RolesAllowed({ "ADMIN", "USER" })
    public Response findRelivesByEmployeesNotAssigned(@DefaultValue("1") @QueryParam("pageNumber") int pageNumber,
            @DefaultValue("10") @QueryParam("pageSize") int pageSize) throws EntityNotFoundException {
        logger.info("Attempting to get all relative list by employees not assigned");

        RelativeListResponseDTO relativeListResponseDTO = this.relativeService
                .findRelivesByEmployeesNotAssigned(pageNumber, pageSize);
        return Response.ok().entity(relativeListResponseDTO).build();
    }
}
