package com.axonactive.dojo.auth.rest;

import com.axonactive.dojo.auth.dto.SignupRequestDTO;
import com.axonactive.dojo.auth.dto.SignupResponseDTO;
import com.axonactive.dojo.auth.dto.VerifyTokeRequestDTO;
import com.axonactive.dojo.auth.service.AuthService;
import com.axonactive.dojo.base.exception.BadRequestException;
import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.base.exception.UnauthorizedException;
import com.axonactive.dojo.base.message.LoggerMessage;
import com.axonactive.dojo.department.dto.CreateDepartmentRequestDTO;
import com.axonactive.dojo.department.dto.DepartmentDTO;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("auth")
public class AuthResource {

    @Inject
    private AuthService authService;

    @POST
    @Path("signup")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Signup")
    @ApiModelProperty
    @ApiResponses({
            @ApiResponse(code = 200, message = "Signup successfully", response = SignupResponseDTO.class),
            @ApiResponse(code = 400, message = "Request sent to the server is invalid"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    public Response signup(@Valid SignupRequestDTO requestDTO) throws BadRequestException {
        SignupResponseDTO responseDTO = authService.signup(requestDTO);
        return Response.ok().entity(responseDTO).build();
    }

    @POST
    @Path("verify")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Verify")
    @ApiModelProperty
    @ApiResponses({
            @ApiResponse(code = 200, message = "Signup successfully", response = SignupResponseDTO.class),
            @ApiResponse(code = 400, message = "Request sent to the server is invalid"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    public Response verify(@Valid VerifyTokeRequestDTO requestDTO) throws UnauthorizedException, EntityNotFoundException {
        authService.verify(requestDTO);
        return Response.noContent().build();
    }
}
