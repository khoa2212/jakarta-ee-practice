package com.axonactive.dojo.employee.rest;

import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.base.message.DeleteSuccessMessage;
import com.axonactive.dojo.base.message.LoggerMessage;
import com.axonactive.dojo.employee.dto.*;
import com.axonactive.dojo.employee.service.EmployeeService;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;


@Path("employees")
@Api(tags = "Employees API")
public class EmployeeResource {

    private static final Logger logger = LogManager.getLogger(EmployeeResource.class);
    @Inject
    private EmployeeService employeeService;

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Get all employee list with pagination")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Get all employee list successfully",
                    response = EmployeeListResponseDTO.class, responseContainer = "List"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Request cannot be fulfilled through browser due to server-side problems"
            )
    })
    public Response findEmployees(@DefaultValue("1") @QueryParam("pageNumber") Integer pageNumber,
                                 @DefaultValue("10") @QueryParam("pageSize") Integer pageSize,
                                 @DefaultValue("0") @QueryParam("departmentId") Long departmentId,
                                 @DefaultValue("") @QueryParam("name") String name) throws EntityNotFoundException {
        logger.info(LoggerMessage.findPaginatedListMessage("employee"));

        EmployeeListResponseDTO employeeListResponseDTO = this.employeeService.findEmployees(departmentId, pageNumber, pageSize, name);
        return Response.ok().entity(employeeListResponseDTO).build();
    }
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Get employee by id")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Get employee successfully",
                    response = EmployeeDTO.class, responseContainer = "List"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Request cannot be fulfilled through browser due to server-side problems"
            )
    })
    public Response findById(@PathParam("id") Long id) throws EntityNotFoundException {
        logger.info(LoggerMessage.findByIdMessage("employee", id));

        EmployeeDTO employeeDTO = this.employeeService.findById(id);
        return Response.ok().entity(employeeDTO).build();
    }

    @POST
    @Path("add")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Create new employee")
    @ApiModelProperty
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Create employee successfully",
                    response = EmployeeDTO.class
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
    public Response add(@Valid CreateEmployeeRequestDTO reqDTO) throws EntityNotFoundException {
        logger.info(LoggerMessage.addMessage(reqDTO.toString()));

        EmployeeDTO employeeDTO = this.employeeService.add(reqDTO);
        return Response.ok(employeeDTO).build();
    }

    @PUT
    @Path("update")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Update employee")
    @ApiModelProperty
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Update employee successfully",
                    response = EmployeeDTO.class
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
    public Response update(@Valid UpdateEmployeeRequestDTO reqDTO) throws EntityNotFoundException {
        logger.info(LoggerMessage.updateMessage(reqDTO.toString()));
        EmployeeDTO employeeDTO = this.employeeService.update(reqDTO);
        return Response.ok().entity(employeeDTO).build();
    }

    @DELETE
    @Path("delete")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Delete employee")
    @ApiModelProperty
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Delete employee successfully",
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
    public Response delete(@Valid DeleteEmployeeRequestDTO reqDTO) throws EntityNotFoundException {
        logger.info(LoggerMessage.deleteMessage(reqDTO.toString()));
        DeleteSuccessMessage result = this.employeeService.deleteSoftly(reqDTO);
        return Response.ok().entity(result).build();
    }

}
