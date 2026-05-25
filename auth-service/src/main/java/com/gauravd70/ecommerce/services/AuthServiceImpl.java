package com.gauravd70.ecommerce.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gauravd70.commons.dtos.GenericResponse;
import com.gauravd70.commons.exceptions.BadRequestException;
import com.gauravd70.commons.exceptions.UnauthorizedException;
import com.gauravd70.commons.filters.JwtType;
import com.gauravd70.commons.filters.JwtUtils;
import com.gauravd70.ecommerce.dtos.LoginRequest;
import com.gauravd70.ecommerce.dtos.RoleEntity;
import com.gauravd70.ecommerce.dtos.SignUpRequest;
import com.gauravd70.ecommerce.dtos.UserEntity;
import com.gauravd70.ecommerce.dtos.UserRoleMappingEntity;
import com.gauravd70.ecommerce.dtos.UserRoleMappingId;
import com.gauravd70.ecommerce.mapper.UserMapper;
import com.gauravd70.ecommerce.repositories.RolesRepository;
import com.gauravd70.ecommerce.repositories.UserRoleMappingsRepository;
import com.gauravd70.ecommerce.repositories.UsersRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final UserRoleMappingsRepository userRoleMappingsRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public ResponseEntity<Void> onLogin(LoginRequest request) throws UnauthorizedException {
        Optional<UserEntity> userEntityOptional = usersRepository.findByUsername(request.getUsername());

        if(userEntityOptional.isEmpty() || !passwordEncoder.matches(request.getPassword(), userEntityOptional.get().getPassword())) {
            throw new UnauthorizedException("Incorrect username or passoword.");
        }

        long userId = userEntityOptional.get().getId();

        UserRoleMappingEntity userRoleMappingEntity = userRoleMappingsRepository.findByIdUserId(userId).orElseThrow(() -> new UnauthorizedException());

        RoleEntity roleEntity = rolesRepository.findById(userRoleMappingEntity.getId().getRoleId()).orElseThrow(() -> new UnauthorizedException()); 

        Map<String, Object> claims = new HashMap<>();

        claims.put("role", roleEntity.getName());

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add(HttpHeaders.SET_COOKIE, jwtUtils.createCookie(JwtType.ACCESS_TOKEN, String.valueOf(userId), claims).toString());
        httpHeaders.add(HttpHeaders.SET_COOKIE, jwtUtils.createCookie(JwtType.REFRESH_TOKEN, String.valueOf(userId), claims).toString());

        return ResponseEntity.ok().headers(httpHeaders).build();
    }

    @Override
    public ResponseEntity<Void> onLogout(String userId) throws UnauthorizedException {
        usersRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new UnauthorizedException());
        
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add(HttpHeaders.SET_COOKIE, jwtUtils.invalidateCookie(JwtType.ACCESS_TOKEN).toString());
        httpHeaders.add(HttpHeaders.SET_COOKIE, jwtUtils.invalidateCookie(JwtType.REFRESH_TOKEN).toString());

        return ResponseEntity.ok().headers(httpHeaders).build();
    }

    @Override
    public GenericResponse onSignUp(SignUpRequest request) throws BadRequestException{
        if(!request.getPassword().equals(request.getConfirmPassword()) || "ADMIN".equalsIgnoreCase(request.getRole())) {
            throw new BadRequestException("Passwords do not match");
        }

        Optional<RoleEntity> roleEntityOptional = rolesRepository.findByName(request.getRole());

        if(roleEntityOptional.isEmpty()) {
            throw new BadRequestException();
        }

        UserEntity userEntity = userMapper.toUserEntity(request, passwordEncoder.encode(request.getPassword()));

        try {
            usersRepository.save(userEntity);
        } catch(DataIntegrityViolationException e) {
            throw new BadRequestException("Username already exists");
        }

        UserRoleMappingEntity userRoleMappingEntity = UserRoleMappingEntity.builder().id(UserRoleMappingId.builder().userId(userEntity.getId()).roleId(roleEntityOptional.get().getId()).build()).build();

        userRoleMappingsRepository.save(userRoleMappingEntity);

        return GenericResponse.builder().message("User created successfully.").build();
    }
}
