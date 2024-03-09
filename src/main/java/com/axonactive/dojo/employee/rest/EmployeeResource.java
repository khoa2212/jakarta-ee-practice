package com.axonactive.dojo.employee.rest;

import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.base.filter.Secure;
import com.axonactive.dojo.base.message.DeleteSuccessMessage;
import com.axonactive.dojo.base.message.LoggerMessage;
import com.axonactive.dojo.employee.dto.*;
import com.axonactive.dojo.employee.service.EmployeeService;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.mail.Header;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


@Path("employees")
@Api(tags = "Employees API")
public class EmployeeResource {

    private static final Logger logger = LogManager.getLogger(EmployeeResource.class);
    @Inject
    private EmployeeService employeeService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Get all employee list with pagination")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Get all employee list successfully", response = EmployeeListResponseDTO.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    public Response findEmployees(@DefaultValue("1") @QueryParam("pageNumber") int pageNumber,
                                 @DefaultValue("10") @QueryParam("pageSize") int pageSize,
                                 @DefaultValue("0") @QueryParam("departmentId") long departmentId,
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
            @ApiResponse(code = 200, message = "Get employee successfully", response = EmployeeDTO.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    public Response findActiveEmployeeById(@PathParam("id") long id) throws EntityNotFoundException {
        logger.info(LoggerMessage.findByIdMessage("employee", id));

        EmployeeDTO employeeDTO = this.employeeService.findActiveEmployeeById(id);
        return Response.ok().entity(employeeDTO).build();
    }

    @GET
    @Path("reports/total-hours/department")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Get employees by hours in project managed by a department")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Get all employee list successfully", response = EmployeeDTO.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    public Response findEmployeesByHoursInProjectMangedByDepartment(@DefaultValue("1") @QueryParam("departmentId") long departmentId,
                                                                    @DefaultValue("1") @QueryParam("pageNumber") int pageNumber,
                                                                    @DefaultValue("10") @QueryParam("pageSize") int pageSize,
                                                                    @DefaultValue("0") @QueryParam("numberOfHour") int numberOfHour) throws EntityNotFoundException {
        logger.info("Attempting get employees by hours in project managed by a department");

        EmployeeListResponseDTO employeeListResponseDTO = this.employeeService.findEmployeesByHoursInProjectMangedByDepartment(departmentId, pageNumber, pageSize, numberOfHour);
        return Response.ok().entity(employeeListResponseDTO).build();
    }

    @POST
    @Path("add")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Create new employee")
    @ApiModelProperty
    @ApiResponses({
            @ApiResponse(code = 201, message = "Create employee successfully", response = EmployeeDTO.class),
            @ApiResponse(code = 400, message = "Request sent to the server is invalid"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    @Secure
    @RolesAllowed({"ADMIN", "USER"})
    public Response add(@Valid CreateEmployeeRequestDTO reqDTO) throws EntityNotFoundException, URISyntaxException {
        logger.info(LoggerMessage.addMessage(reqDTO.toString()));

        EmployeeDTO employeeDTO = this.employeeService.add(reqDTO);

        String path = String.format("%s/%d", uriInfo.getAbsolutePath().getPath(), employeeDTO.getId());

        return Response.created(URI.create(path)).entity(employeeDTO).build();
    }

    @PUT
    @Path("update")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Update employee")
    @ApiModelProperty
    @ApiResponses({
            @ApiResponse(code = 201, message = "Update employee successfully", response = EmployeeDTO.class),
            @ApiResponse(code = 400, message = "Request sent to the server is invalid"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    @Secure
    @RolesAllowed({"ADMIN", "USER"})
    public Response update(@Valid UpdateEmployeeRequestDTO reqDTO) throws EntityNotFoundException {
        logger.info(LoggerMessage.updateMessage(reqDTO.toString()));

        EmployeeDTO employeeDTO = this.employeeService.update(reqDTO);
        String path = String.format("%s/%d", uriInfo.getAbsolutePath().getPath(), employeeDTO.getId());

        return Response.created(URI.create(path)).entity(employeeDTO).build();
    }

    @DELETE
    @Path("delete")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Delete employee")
    @ApiModelProperty
    @ApiResponses({
            @ApiResponse(code = 204, message = "Delete employee successfully"),
            @ApiResponse(code = 400, message = "Request sent to the server is invalid"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    @Secure
    @RolesAllowed({"ADMIN"})
    public Response delete(@Valid DeleteEmployeeRequestDTO reqDTO) throws EntityNotFoundException {
        logger.info(LoggerMessage.deleteMessage(reqDTO.toString()));
        DeleteSuccessMessage result = this.employeeService.deleteSoftly(reqDTO);
        return Response.noContent().build();
    }

}
