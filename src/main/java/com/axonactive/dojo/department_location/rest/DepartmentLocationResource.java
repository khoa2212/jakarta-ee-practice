package com.axonactive.dojo.department_location.rest;

import com.axonactive.dojo.assignment.dto.AssignmentDTO;
import com.axonactive.dojo.assignment.dto.AssignmentListResponseDTO;
import com.axonactive.dojo.base.exception.BadRequestException;
import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.base.message.DeleteSuccessMessage;
import com.axonactive.dojo.department_location.dto.*;
import com.axonactive.dojo.department_location.service.DepartmentLocationService;
import io.swagger.annotations.*;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("department-locations")
@Api(tags = "Department locations API")
public class DepartmentLocationResource {


    @Inject
    private DepartmentLocationService departmentLocationService;

    @GET
    @Path("department/{departmentId}")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Get all department location list with pagination")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Get all department location list successfully",
                    response = DepartmentLocationListResponseDTO.class, responseContainer = "List"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Request cannot be fulfilled through browser due to server-side problems"
            )
    })
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
    @ApiOperation(value = "Create new department location")
    @ApiModelProperty
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Create department location successfully",
                    response = DepartmentLocationDTO.class
            ),
            @ApiResponse(
                    code = 400,
                    message = "Request sent to the server is invalid"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Request cannot be fulfilled through browser due to server-side problems"
            )
    })
    public Response add(@Valid CreateDepartmentLocationRequestDTO createDepartmentLocationRequestDTO) throws BadRequestException, EntityNotFoundException {
        DepartmentLocationDTO departmentLocationDTO = this.departmentLocationService.add(createDepartmentLocationRequestDTO);
        return Response.ok().entity(departmentLocationDTO).build();
    }

    @PUT
    @Path("update")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Update department location")
    @ApiModelProperty
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Update department location successfully",
                    response = DepartmentLocationDTO.class
            ),
            @ApiResponse(
                    code = 400,
                    message = "Request sent to the server is invalid"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Request cannot be fulfilled through browser due to server-side problems"
            )
    })
    public Response update(@Valid UpdateDepartmentLocationRequestDTO updateDepartmentLocationRequestDTO) throws BadRequestException, EntityNotFoundException {
        DepartmentLocationDTO departmentLocationDTO = this.departmentLocationService.update(updateDepartmentLocationRequestDTO);
        return Response.ok().entity(departmentLocationDTO).build();
    }

    @DELETE
    @Path("delete")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Delete department location")
    @ApiModelProperty
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Delete department location successfully",
                    response = DeleteSuccessMessage.class
            ),
            @ApiResponse(
                    code = 400,
                    message = "Request sent to the server is invalid"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Request cannot be fulfilled through browser due to server-side problems"
            )
    })
    public Response delete(@Valid DeleteDepartmentLocationRequestDTO deleteDepartmentLocationRequestDTO) throws EntityNotFoundException {
        DeleteSuccessMessage result = this.departmentLocationService.delete(deleteDepartmentLocationRequestDTO);
        return Response.ok().entity(result).build();
    }
}
