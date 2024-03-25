package com.axonactive.dojo.auth.service;

import com.axonactive.dojo.auth.cache.AuthCache;
import com.axonactive.dojo.auth.dao.AuthDAO;
import com.axonactive.dojo.auth.dto.*;
import com.axonactive.dojo.auth.message.AuthMessage;
import com.axonactive.dojo.base.dao.BaseDAO;
import com.axonactive.dojo.base.email.EmailService;
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

    @Inject
    private EmailService emailService;

    @Inject
    private AuthCache authCache;

    private static final int DEFAULT_MAX_INVALID_CREDENTIALS = 3;

    private void blockEmailWhenReach3TimesBadCredential(Optional<User> user, String email) {
        if(authCache.getCacheValue(email) == null) {
            authCache.setBadCredentialTimes(email);
            return;
        }

        if(authCache.getCacheValue(email).size() == DEFAULT_MAX_INVALID_CREDENTIALS) {
            if(user.isEmpty()) {
                authDAO.add(User.builder().email(email)
                        .role(Role.USER)
                        .displayName("BLOCKED USER")
                        .password("BLOCKED")
                        .status(Status.BLOCKED).build());
            }
            else {
                user.get().setStatus(Status.BLOCKED);
                authDAO.update(user.get());
            }
            authCache.setBadCredentialTimes(email);
        }
        else if(authCache.getCacheValue(email).size() < DEFAULT_MAX_INVALID_CREDENTIALS) {
            authCache.setBadCredentialTimes(email);
        }

    }

    public LoginResponseDTO login(LoginRequestDTO requestDTO) throws BadRequestException {
        Optional<User> user = authDAO.findActiveUserByEmail(requestDTO.getEmail());

        if(user.isEmpty()) {
            blockEmailWhenReach3TimesBadCredential(user, requestDTO.getEmail());

            int tries = DEFAULT_MAX_INVALID_CREDENTIALS + 1 - authCache.getCacheValue(requestDTO.getEmail()).size();

            throw new BadRequestException(AuthMessage.badCredentialMessage(Math.max(tries, 0)));
        }

        boolean isValidPassword = BCrypt.checkpw(requestDTO.getPassword(), user.get().getPassword());

        if(!isValidPassword) {
            blockEmailWhenReach3TimesBadCredential(user, requestDTO.getEmail());

            int tries = DEFAULT_MAX_INVALID_CREDENTIALS + 1 - authCache.getCacheValue(requestDTO.getEmail()).size();

            throw new BadRequestException(AuthMessage.badCredentialMessage(Math.max(tries, 0)));
        }

        String accessToken = jwtService.generateToken(TokenPayload
                .builder()
                .tokenType(TokenType.ACCESS_TOKEN)
                .displayName(user.get().getDisplayName())
                .email(user.get().getEmail())
                .role(user.get().getRole())
                .build());

        String refreshToken = jwtService.generateToken(TokenPayload
                .builder()
                .tokenType(TokenType.REFRESH_TOKEN)
                .displayName(user.get().getDisplayName())
                .email(user.get().getEmail())
                .role(user.get().getRole())
                .build());

        user.get().setRefreshToken(refreshToken);

        authDAO.update(user.get());

        return LoginResponseDTO
                .builder()
                .displayName(user.get().getDisplayName())
                .email(user.get().getEmail())
                .avatar(user.get().getAvatar())
                .role(user.get().getRole())
                .accessToken(accessToken)
                .refreshToken(user.get().getRefreshToken())
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

        emailService.sendEmail(newUser.getEmail(), newUser.getDisplayName(), verifiedToken);

        return SignupResponseDTO
                .builder()
                .verifiedToken(verifiedToken)
                .build();
    }

    public LoginResponseDTO verify(VerifyTokeRequestDTO requestDTO) throws UnauthorizedException, EntityNotFoundException {
        TokenPayload payload = jwtService.verifyToken(requestDTO.getVerifiedToken(), TokenType.VERIFY_TOKEN);

        User user = authDAO.findUserByEmail(payload.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(AuthMessage.NOT_FOUND_USER));

        user.setStatus(Status.ACTIVE);

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
