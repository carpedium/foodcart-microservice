// AuthController: Handles registration, login, and profile retrieval for auth-service

package com.foodcart.authservice.controller;

import com.foodcart.authservice.dto.AuthResDto;
import com.foodcart.authservice.dto.LoginReqDto;
import com.foodcart.authservice.dto.RegisterReqDto;
import com.foodcart.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication operations:
 * - Register a new user
 * - Login with credentials
 * - Fetch authenticated user profile
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Registers a new user in Keycloak
     * @param req registration request (username/email/password)
     * @return AuthResDto with tokens
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResDto> register(@Valid @RequestBody RegisterReqDto req) {
        AuthResDto res = authService.register(req);
        return ResponseEntity.ok(res);
    }

    /**
     * Logs in the user and returns access/refresh tokens
     * @param req login request (username/password)
     * @return AuthResDto with token payloads
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResDto> login(@Valid @RequestBody LoginReqDto req) {
        AuthResDto res = authService.login(req);
        return ResponseEntity.ok(res);
    }

    /**
     * Returns the authenticated user’s information based on JWT token
     * @param principal injected JWT
     * @return basic user profile (id, username, roles)
     */
    @GetMapping("/me")
    public ResponseEntity<AuthResDto> getProfile(@AuthenticationPrincipal Jwt principal) {
        AuthResDto res = authService.getProfile(principal);
        return ResponseEntity.ok(res);
    }
}
