package com.foodcart.authservice.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.foodcart.authservice.dto.AuthResDto;
import com.foodcart.authservice.dto.LoginReqDto;
import com.foodcart.authservice.dto.RegisterReqDto;
import com.foodcart.authservice.exception.InvalidCredentialsException;
import com.foodcart.authservice.exception.UserAlreadyExistsException;
import com.foodcart.authservice.service.AuthService;
import com.foodcart.authservice.util.KeycloakUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final KeycloakUtil keycloakUtil;

    @Value("${keycloak.realm}")
    private String realm;

    @Override
    public AuthResDto register(RegisterReqDto req) {
        boolean created = keycloakUtil.createUser(req);
        if (!created) {
            throw new UserAlreadyExistsException();
        }
        // Auto login after registration (optional)
        return login(new LoginReqDto(req.getUsername(), req.getPassword()));
    }

    @Override
    public AuthResDto login(LoginReqDto req) {
        return keycloakUtil.getToken(req)
                .orElseThrow(InvalidCredentialsException::new);
    }

    @Override
    public AuthResDto getProfile(Jwt principal) {
        return AuthResDto.builder()
                .id(principal.getSubject())
                .username(principal.getClaim("preferred_username"))
                .roles(principal.getClaimAsStringList("realm_access.roles"))
                .message("Authenticated user profile")
                .build();
    }
}
