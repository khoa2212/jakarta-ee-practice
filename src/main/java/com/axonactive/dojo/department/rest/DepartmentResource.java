package com.axonactive.dojo.department.rest;

import com.axonactive.dojo.department.dto.DepartmentListResponse;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.department.service.DepartmentService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("departments")
public class DepartmentResource {

    @Inject
    DepartmentService departmentService;

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public Response getDepartments(@DefaultValue("1") @QueryParam("pageNumber") Integer pageNumber,
                                           @DefaultValue("10") @QueryParam("pageSize") Integer pageSize) {
        DepartmentListResponse departmentListResponse = this.departmentService.findDepartments(pageNumber, pageSize);
        return Response.ok().entity(departmentListResponse).build();
    }

//    @POST
//    @Path("create")
//    @Produces({MediaType.APPLICATION_JSON})
//    public Response createDepartments(Department department) {
//        var addedDep = departmentService.createDepartment(department);
//        return Response.ok(addedDep).build();
//    }
}
