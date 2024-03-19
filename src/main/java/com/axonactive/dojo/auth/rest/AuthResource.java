package com.axonactive.dojo.auth.rest;

import com.axonactive.dojo.auth.dto.*;
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

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;

@Path("auth")
public class AuthResource {

    @Inject
    private AuthService authService;

    @POST
    @Path("login")
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Signup")
    @ApiModelProperty
    @ApiResponses({
            @ApiResponse(code = 200, message = "Signup successfully", response = SignupResponseDTO.class),
            @ApiResponse(code = 400, message = "Request sent to the server is invalid"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    public Response login(@Valid LoginRequestDTO requestDTO) throws BadRequestException {
        LoginResponseDTO responseDTO = authService.login(requestDTO);
        return Response.ok().entity(responseDTO).build();
    }

    @POST
    @Path("signup")
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
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
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Verify")
    @ApiModelProperty
    @ApiResponses({
            @ApiResponse(code = 200, message = "Signup successfully", response = SignupResponseDTO.class),
            @ApiResponse(code = 400, message = "Request sent to the server is invalid"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    public Response verify(@Valid VerifyTokeRequestDTO requestDTO)
            throws UnauthorizedException, EntityNotFoundException {
        authService.verify(requestDTO);
        return Response.noContent().build();
    }

    @POST
    @Path("renew-token")
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Verify")
    @ApiModelProperty
    @ApiResponses({
            @ApiResponse(code = 200, message = "Signup successfully", response = SignupResponseDTO.class),
            @ApiResponse(code = 400, message = "Request sent to the server is invalid"),
            @ApiResponse(code = 500, message = "Request cannot be fulfilled through browser due to server-side problems")
    })
    public Response renew(@Valid RenewAccessTokenRequestDTO requestDTO) throws UnauthorizedException {
        RenewAccessTokenResponseDTO responseDTO = authService.renew(requestDTO);

        return Response.ok().entity(responseDTO).build();
    }
}
