package com.axonactive.dojo.project.rest;

import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.base.message.LoggerMessage;
import com.axonactive.dojo.department.dto.DepartmentDTO;
import com.axonactive.dojo.department.dto.DepartmentListResponseDTO;
import com.axonactive.dojo.employee.rest.EmployeeResource;
import com.axonactive.dojo.project.dto.ProjectDTO;
import com.axonactive.dojo.project.dto.ProjectListResponseDTO;
import com.axonactive.dojo.project.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.interfaces.RSAKey;
import java.util.List;

@Path("projects")
@Api(tags = "Projects API")
public class ProjectResource {

    private static final Logger logger = LogManager.getLogger(ProjectResource.class);

    @Inject
    private ProjectService projectService;

    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Get all projects list")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Get all projects list successfully", response = ProjectDTO.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    public Response findAll() {
        logger.info(LoggerMessage.findAllMessage("project"));

        List<ProjectDTO> projectDTOS = this.projectService.findAll();
        return Response.ok().entity(projectDTOS).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Get all project list with pagination")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Get all project list successfully", response = ProjectListResponseDTO.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    public Response findProjects(@DefaultValue("1") @QueryParam("pageNumber") int pageNumber,
                                 @DefaultValue("10") @QueryParam("pageSize") int pageSize,
                                 @DefaultValue("0") @QueryParam("departmentId") long departmentId) throws EntityNotFoundException {
        logger.info(LoggerMessage.findPaginatedListMessage("project"));

        ProjectListResponseDTO projectListResponseDTO = this.projectService.findProjects(departmentId, pageNumber, pageSize);
        return Response.ok().entity(projectListResponseDTO).build();
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Get project by id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Get project successfully", response = ProjectDTO.class),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    public Response findById(@PathParam("id") long id) throws EntityNotFoundException {
        logger.info(LoggerMessage.findByIdMessage("project", id));

        ProjectDTO projectDTO = this.projectService.findById(id);
        return Response.ok().entity(projectDTO).build();
    }
}
