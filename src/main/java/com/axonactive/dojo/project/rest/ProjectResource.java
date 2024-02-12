package com.axonactive.dojo.project.rest;

import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.project.dto.ProjectDTO;
import com.axonactive.dojo.project.dto.ProjectListResponseDTO;
import com.axonactive.dojo.project.service.ProjectService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.interfaces.RSAKey;
import java.util.List;

@Path("projects")
public class ProjectResource {

    @Inject
    private ProjectService projectService;

    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAll() {
        List<ProjectDTO> projectDTOS = this.projectService.findAll();

        return Response.ok().entity(projectDTOS).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findProjects(@DefaultValue("1") @QueryParam("pageNumber") Integer pageNumber,
                                 @DefaultValue("10") @QueryParam("pageSize") Integer pageSize,
                                 @DefaultValue("0") @QueryParam("departmentId") Long departmentId) throws EntityNotFoundException {
        ProjectListResponseDTO projectListResponseDTO = this.projectService.findProjects(departmentId, pageNumber, pageSize);
        return Response.ok().entity(projectListResponseDTO).build();
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findById(@PathParam("id") Long id) throws EntityNotFoundException {
        ProjectDTO projectDTO = this.projectService.findById(id);
        return Response.ok().entity(projectDTO).build();
    }
}
