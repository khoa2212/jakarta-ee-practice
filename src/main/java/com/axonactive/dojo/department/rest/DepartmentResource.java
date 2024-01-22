package com.axonactive.dojo.department.rest;

import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.department.service.DepartmentService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("department")
public class DepartmentResource {

    @Inject
    DepartmentService departmentService;

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public List<Department> getDepartments() {
        return departmentService.getDepartments();
    }

//    @POST
//    @Path("create")
//    @Produces({MediaType.APPLICATION_JSON})
//    public Response createDepartments(Department department) {
//        var addedDep = departmentService.createDepartment(department);
//        return Response.ok(addedDep).build();
//    }
}
