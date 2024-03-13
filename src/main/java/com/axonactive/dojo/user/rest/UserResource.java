package com.axonactive.dojo.user.rest;

import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.base.filter.Secure;
import com.axonactive.dojo.base.message.LoggerMessage;
import com.axonactive.dojo.user.dto.UserListResponseDTO;
import com.axonactive.dojo.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("users")
public class UserResource {

    private static final Logger logger = LogManager.getLogger(UserResource.class);

    @Inject
    private UserService userService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Get all user list with pagination")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Get all user list successfully", response = UserListResponseDTO.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    @Secure
    @RolesAllowed({"ADMIN"})
    public Response findActiveAndInActiveUsers(@DefaultValue("1") @QueryParam("pageNumber") int pageNumber,
                                              @DefaultValue("10") @QueryParam("pageSize") int pageSize) throws EntityNotFoundException {
        logger.info(LoggerMessage.findPaginatedListMessage("user"));

        UserListResponseDTO userListResponseDTO = this.userService.findActiveAndInActiveUsers(pageNumber, pageSize);
        return Response.ok().entity(userListResponseDTO).build();
    }
}
