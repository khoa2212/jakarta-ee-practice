package com.axonactive.dojo.auth.service;

import com.axonactive.dojo.auth.dao.AuthDAO;
import com.axonactive.dojo.auth.dto.*;
import com.axonactive.dojo.auth.message.AuthMessage;
import com.axonactive.dojo.base.dao.BaseDAO;
import com.axonactive.dojo.base.exception.BadRequestException;
import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.base.exception.UnauthorizedException;
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

    public LoginResponseDTO login(LoginRequestDTO requestDTO) throws BadRequestException {
        User user = authDAO
                .findActiveUserByEmail(requestDTO.getEmail())
                .orElseThrow(() -> new BadRequestException(AuthMessage.PASSWORD_OR_EMAIL_IS_NOT_CORRECT));

        boolean isValidPassword = BCrypt.checkpw(requestDTO.getPassword(), user.getPassword());

        if(!isValidPassword) {
            throw new BadRequestException(AuthMessage.PASSWORD_OR_EMAIL_IS_NOT_CORRECT);
        }



        String accessToken = jwtService.generateToken(TokenPayload
                .builder()
                .tokenType(TokenType.ACCESS_TOKEN)
                .displayName(user.getDisplayName())
                .email(user.getEmail())
                .role(user.getRole())
                .build());

        String refreshToken = jwtService.generateToken(TokenPayload
                .builder()
                .tokenType(TokenType.REFRESH_TOKEN)
                .displayName(user.getDisplayName())
                .email(user.getEmail())
                .role(user.getRole())
                .build());

        user.setRefreshToken(refreshToken);

        authDAO.update(user);

        return LoginResponseDTO
                .builder()
                .displayName(user.getDisplayName())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .accessToken(accessToken)
                .refreshToken(user.getRefreshToken())
                .build();
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

    public void verify(VerifyTokeRequestDTO requestDTO) throws UnauthorizedException, EntityNotFoundException {
        TokenPayload payload = jwtService.verifyToken(requestDTO.getVerifiedToken(), TokenType.VERIFY_TOKEN);

        User user = authDAO.findUserByEmail(payload.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(AuthMessage.NOT_FOUND_USER));

        user.setStatus(Status.ACTIVE);

        authDAO.update(user);
    }

    public RenewAccessTokenResponseDTO renew(RenewAccessTokenRequestDTO requestDTO) throws UnauthorizedException {
        TokenPayload payload = jwtService.verifyToken(requestDTO.getRefreshToken(), TokenType.REFRESH_TOKEN);

        User user = authDAO.findActiveUserByEmail(payload.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid token"));

        if(!Objects.equals(user.getRefreshToken(), requestDTO.getRefreshToken())) {
            throw new UnauthorizedException("Invalid token");
        }

        payload.setTokenType(TokenType.ACCESS_TOKEN);

        String accessToken = jwtService.generateToken(payload);

        return RenewAccessTokenResponseDTO.builder().accessToken(accessToken).build();
    }
 }
