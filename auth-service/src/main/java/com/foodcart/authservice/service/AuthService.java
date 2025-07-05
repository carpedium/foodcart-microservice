package com.foodcart.authservice.service;

import org.springframework.security.oauth2.jwt.Jwt;

import com.foodcart.authservice.dto.AuthResDto;
import com.foodcart.authservice.dto.LoginReqDto;
import com.foodcart.authservice.dto.RegisterReqDto;

public interface AuthService {
    AuthResDto register(RegisterReqDto req);
    AuthResDto login(LoginReqDto req);
    AuthResDto getProfile(Jwt principal);
}