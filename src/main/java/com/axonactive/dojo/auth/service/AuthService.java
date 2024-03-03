package com.axonactive.dojo.auth.service;

import com.axonactive.dojo.auth.dao.AuthDAO;
import com.axonactive.dojo.auth.dto.*;
import com.axonactive.dojo.auth.message.AuthMessage;
import com.axonactive.dojo.base.dao.BaseDAO;
import com.axonactive.dojo.base.exception.BadRequestException;
import com.axonactive.dojo.base.jwt.payload.TokenPayload;
import com.axonactive.dojo.base.jwt.service.JwtService;
import com.axonactive.dojo.enums.Role;
import com.axonactive.dojo.enums.Status;
import com.axonactive.dojo.enums.TokenType;
import com.axonactive.dojo.user.entity.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;

@Stateless
public class AuthService {

    @Inject
    private AuthDAO authDAO;

    @Inject
    private JwtService jwtService;

    public LoginResponseDTO login(LoginRequestDTO requestDTO) {

        return LoginResponseDTO.builder().build();
    }

    public SignupResponseDTO signup(SignupRequestDTO requestDTO) throws BadRequestException {
        Optional<User> user = authDAO.findUserByEmail(requestDTO.getEmail());

        if (user.isPresent()) {
            throw new BadRequestException(AuthMessage.EXISTED_EMAIL);
        }

        if (!Objects.equals(requestDTO.getPassword(), requestDTO.getConfirmedPassword())) {
            throw new BadRequestException(AuthMessage.PASSWORDS_MUST_MATCH);
        }

        String salt = BCrypt.gensalt();

        String hashPass = BCrypt.hashpw(requestDTO.getPassword(), salt);

        User newUser = User.builder()
                .displayName(requestDTO.getDisplayName())
                .password(hashPass)
                .role(Role.USER)
                .status(Status.INACTIVE)
                .email(requestDTO.getEmail()).build();

        authDAO.add(newUser);

        String verifiedToken = jwtService.generateToken(
                TokenPayload
                .builder()
                .tokenType(TokenType.VERIFY_TOKEN)
                .displayName(newUser.getDisplayName())
                .email(newUser.getEmail())
                .role(newUser.getRole())
                .build()
        );

        return SignupResponseDTO
                .builder()
                .verifiedToken(verifiedToken)
                .build();
    }

    public void verify(VerifyRequestDTO requestDTO) {

    }

    public RenewAccessTokenResponseDTO renew(RenewAccessTokenRequestDTO requestDTO) {
        return RenewAccessTokenResponseDTO.builder().build();
    }
 }
