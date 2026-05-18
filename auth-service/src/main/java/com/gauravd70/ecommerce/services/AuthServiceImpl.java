package com.gauravd70.ecommerce.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gauravd70.commons.dtos.GenericResponse;
import com.gauravd70.commons.exceptions.BadRequestException;
import com.gauravd70.commons.exceptions.UnauthorizedException;
import com.gauravd70.commons.security.JwtType;
import com.gauravd70.commons.security.JwtUtils;
import com.gauravd70.ecommerce.dtos.LoginRequest;
import com.gauravd70.ecommerce.dtos.Roles;
import com.gauravd70.ecommerce.dtos.SignUpRequest;
import com.gauravd70.ecommerce.dtos.UserEntity;
import com.gauravd70.ecommerce.mapper.UserMapper;
import com.gauravd70.ecommerce.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public Mono<ResponseEntity<Void>> onLogin(LoginRequest request) {
        return Mono.fromCallable(() -> userRepository.findByUsername(request.getUsername()))
            .filter(userEntityOptional -> userEntityOptional.isPresent() && passwordEncoder.matches(request.getPassword(), userEntityOptional.get().getPassword()))
            .switchIfEmpty(Mono.error(new UnauthorizedException("Incorrect username or passoword.")))
            .map(userEntityOptional -> {
                UserEntity userEntity = userEntityOptional.get();
                String userId = String.valueOf(userEntity.getId());

                Map<String, Object> claims = new HashMap<>();
                claims.put("roles", userEntity.getRoles());

                HttpHeaders httpHeaders = new HttpHeaders();

                httpHeaders.add(HttpHeaders.SET_COOKIE, jwtUtils.createCookie(JwtType.ACCESS_TOKEN, userId, claims).toString());
                httpHeaders.add(HttpHeaders.SET_COOKIE, jwtUtils.createCookie(JwtType.REFRESH_TOKEN, userId, claims).toString());

                return httpHeaders;
            })
            .map(httpHeaders -> ResponseEntity.ok().headers(httpHeaders).build());
    }

    @Override
    public Mono<ResponseEntity<Void>> onLogout(String userId) {
        return Mono.fromCallable(() -> userRepository.findById(Long.parseLong(userId)))
            .filter(userEntityOptional -> userEntityOptional.isPresent())
            .switchIfEmpty(Mono.error(new UnauthorizedException()))
            .map(userEntityOptional -> {
                HttpHeaders httpHeaders = new HttpHeaders();

                httpHeaders.add(HttpHeaders.SET_COOKIE, jwtUtils.invalidateCookie(JwtType.ACCESS_TOKEN).toString());
                httpHeaders.add(HttpHeaders.SET_COOKIE, jwtUtils.invalidateCookie(JwtType.REFRESH_TOKEN).toString());

                return httpHeaders;
            })
            .map(httpHeaders -> ResponseEntity.ok().headers(httpHeaders).build());

    }

    @Override
    public Mono<GenericResponse> onSignUp(SignUpRequest request) {
        return Mono.just(request)
            .filter(req -> req.getPassword().equals(req.getConfirmPassword()))
            .switchIfEmpty(Mono.error(new BadRequestException("Passwords do not match")))
            .map(req -> userMapper.toUserEntity(req, List.of(Roles.USER.name()), passwordEncoder.encode(req.getPassword())))
            .flatMap(user -> Mono.fromCallable(() -> userRepository.save(user)).subscribeOn(Schedulers.boundedElastic()))
            .onErrorResume(DataIntegrityViolationException.class, e -> Mono.error(new BadRequestException("Username already exists")))
            .map(userEntity -> GenericResponse.builder().message("User created successfully.").build());
    }
}
