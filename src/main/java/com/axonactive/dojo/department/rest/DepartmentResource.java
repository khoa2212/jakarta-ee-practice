package com.axonactive.dojo.department.rest;

import com.axonactive.dojo.base.exception.BadRequestException;
import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.base.message.DeleteSuccessMessage;
import com.axonactive.dojo.base.message.LoggerMessage;
import com.axonactive.dojo.department.dto.*;
import com.axonactive.dojo.department.service.DepartmentService;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("departments")
@Api(tags = "Departments API")
public class DepartmentResource {

    @Inject
    private DepartmentService departmentService;

    private static final Logger logger = LogManager.getLogger(DepartmentResource.class);

    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Get all department list")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Get all department list successfully", response = DepartmentDTO.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    public Response findAll() {
        logger.info(LoggerMessage.findAllMessage("department"));

        List<DepartmentDTO> departmentDTOS = this.departmentService.findAll();
        return Response.ok().entity(departmentDTOS).build();
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Get all department list with pagination")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Get all department list successfully", response = DepartmentListResponseDTO.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    public Response findDepartments(@DefaultValue("1") @QueryParam("pageNumber") Integer pageNumber,
                                           @DefaultValue("10") @QueryParam("pageSize") Integer pageSize) {
        logger.info(LoggerMessage.findPaginatedListMessage("department"));

        DepartmentListResponseDTO departmentListResponseDTO = this.departmentService.findDepartments(pageNumber, pageSize);
        return Response.ok().entity(departmentListResponseDTO).build();
    }

    @GET
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Get department by id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Get department successfully", response = DepartmentDTO.class),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    public Response findById(@PathParam("id") Long id) throws EntityNotFoundException {
        logger.info(LoggerMessage.findByIdMessage("department", id));

        DepartmentDTO departmentDTO = this.departmentService.findById(id);
        return Response.ok().entity(departmentDTO).build();
    }

    @POST
    @Path("add")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Create new department")
    @ApiModelProperty
    @ApiResponses({
            @ApiResponse(code = 200, message = "Create department successfully", response = DepartmentDTO.class),
            @ApiResponse(code = 400, message = "Request sent to the server is invalid"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    public Response add(@Valid CreateDepartmentRequestDTO createDepartmentRequestDTO) throws BadRequestException {
        logger.info(LoggerMessage.addMessage(createDepartmentRequestDTO.toString()));

        DepartmentDTO departmentDTO = this.departmentService.add(createDepartmentRequestDTO);
        return Response.ok().entity(departmentDTO).build();
    }

    @PUT
    @Path("update")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Update department ")
    @ApiModelProperty
    @ApiResponses({
            @ApiResponse(code = 200, message = "Update department successfully", response = DepartmentDTO.class),
            @ApiResponse(code = 400, message = "Request sent to the server is invalid"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    public Response update(@Valid UpdateDepartmentRequestDTO updateDepartmentRequestDTO) throws EntityNotFoundException, BadRequestException {
        logger.info(LoggerMessage.updateMessage(updateDepartmentRequestDTO.toString()));

        DepartmentDTO departmentDTO = this.departmentService.update(updateDepartmentRequestDTO);
        return Response.ok().entity(departmentDTO).build();
    }

    @DELETE
    @Path("delete")
    @ApiOperation(value = "Delete softly department")
    @ApiModelProperty
    @ApiResponses({
            @ApiResponse(code = 200, message = "Delete department successfully", response = DeleteSuccessMessage.class),
            @ApiResponse(code = 400, message = "Request sent to the server is invalid"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response delete(@Valid DeleteDepartmentRequestDTO deleteDepartmentRequestDTO) throws EntityNotFoundException, BadRequestException {
        logger.info(LoggerMessage.deleteMessage(deleteDepartmentRequestDTO.toString()));

        DeleteSuccessMessage result = this.departmentService.deleteSoftly(deleteDepartmentRequestDTO);
        return Response.ok().entity(result).build();
    }
}
