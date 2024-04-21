package com.axonactive.dojo.demo_kafka;

import com.axonactive.dojo.base.exception.EntityNotFoundException;
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

@Path("demo-kafka")
public class DemoKafkaResource {

    @Inject
    private DemoKafkaService demoKafkaService;

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
    public Response demoRabbitMQ(@PathParam("employeeId") long employeeId, @QueryParam("key") String key) throws EntityNotFoundException, IOException, TimeoutException {
        this.demoKafkaService.demoKafka(employeeId, key);
        return Response.noContent().build();
    }
}
