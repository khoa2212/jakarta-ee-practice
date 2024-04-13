package com.axonactive.dojo.demo_rabbitmq;

import com.axonactive.dojo.base.email.EmailService;
import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.employee.service.EmployeeService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Path("demo")
public class DemoRabbitMQResource {

    @Inject
    private DemoRabbitMQService demoRabbitMQService;

    @POST
    @Path("send-message/{employeeId}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Send message")
    @ApiModelProperty
    @ApiResponses({
            @ApiResponse(code = 204, message = ""),
            @ApiResponse(code = 400, message = "Request sent to the server is invalid"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    public Response demoRabbitMQ(@PathParam("employeeId") long employeeId, @QueryParam("exchange") String exchange) throws EntityNotFoundException, IOException, TimeoutException {
        this.demoRabbitMQService.demoRabbitMQ(employeeId, exchange);
        return Response.noContent().build();
    }
}
