package org.eclipse.jakarta.department.controller;

import org.eclipse.jakarta.department.entity.Department;
import org.eclipse.jakarta.department.service.DepartmentService;
import org.eclipse.jakarta.employee.entity.Employee;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("department")
public class DepartmentController {

    @Inject
    DepartmentService departmentService;

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public List<Department> getDepartments() {
        return departmentService.getDepartments();
    }

    @POST
    @Path("create")
    @Produces({MediaType.APPLICATION_JSON})
    public Response createDepartments(Department department) {
        departmentService.createDepartment(department);
        return Response.ok(department).build();
    }
}
