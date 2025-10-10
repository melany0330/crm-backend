/*
BSD 3-Clause License

Copyright (c) 2025, WMS

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

3. Neither the name of the copyright holder nor the names of its
   contributors may be used to endorse or promote products derived from
   this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.wms.service.auth;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.wms.model.auth.LoginRequest;
import org.wms.model.auth.RegisterRequest;
import org.wms.model.auth.Token;
import org.wms.model.auth.TokenResponse;
import org.wms.model.auth.User;

import org.wms.repository.auth.RoleRepository;
import org.wms.repository.auth.TokenRepository;
import org.wms.repository.auth.UserRepository;
import static org.wms.core.system.Checks.*;

/**
 * Clase encargada de gestionar el servicios de autenticación.
 *
 * @author wil
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    // *************************************************************************
    // variables finales
    
    /** Repositorio de usuarios */
    private final UserRepository userRepository;
    /** Repositorio de tokens */
    private final TokenRepository tokenRepository;
    /** Repositorio de roles */
    private final RoleRepository roleRepository;
    /** Codificador de credenciales */
    private final PasswordEncoder passwordEncoder;
    /** Servicios JWT */
    private final JwtService jwtService;
    /** Administrador de sesiones. */
    private final AuthenticationManager authenticationManager;
    
    // *************************************************************************
    // implementaciones
    
    /**
     * Registra a un nuevo usuarios si no existe en la base de datos.
     *
     * @param request datos requeridos
     *
     * @return el toeken de acceso
     */
    public TokenResponse register(RegisterRequest request) {
        if (! checkUniqueUser(request.userName())) {
            throw new IllegalArgumentException("Existing user");
        }
        
        var role = roleRepository.findById(request.role())
                                 .orElseThrow();
        var user = User.builder()
                .name(request.userName())
                .password(passwordEncoder.encode(request.password()))
                .status(true)
                .rol(role)
                .build();
        
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(savedUser);
        var refreshToken = jwtService.generateRefreshToken(savedUser);
        
        saveUserToken(savedUser, jwtToken);
        return new TokenResponse(jwtToken, refreshToken);
    }
    
    /**
     * Loguea a un usuarios existente en el sistema.
     *
     * @param request datos requeridos
     *
     * @return el token de acceso
     */
    public TokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.userName(),
                        request.password()
                )
        );
        
        var user = userRepository.findByUserName(request.userName())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        
        revokeAllUserToken(user);
        saveUserToken(user, jwtToken);
        
        return new TokenResponse(jwtToken, refreshToken);
    }
    
    /**
     * Vericia si el usuario ya existe en la base de datos.
     *
     * @param userName el nombre del usauior
     *
     * @return {@code true} si existe en el sistema, de lo contrarios {@code false}
     */
    private boolean checkUniqueUser(String userName) {
        var user = userRepository.findByUserName(userName);
        return user.isEmpty();
    }
    
    /**
     * Guarda un nuevo token de un usuario específico.
     *
     * @param user usuario del token
     *
     * @param jwtToken el token
     */
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoke(false)
                .tokenType(Token.TokenType.BEARER)
                .build();
        
        tokenRepository.save(token);
    }
    
    /**
     * Elimina todos los acceso a un usuarios específico.
     *
     * @param user el usuarios
     */
    private void revokeAllUserToken(User user) {
        final List<Token> validUserTokens = tokenRepository
                .findAllValidIsFalseOrRevokedIsFalseByUserId(user.getId());
        if (! validUserTokens.isEmpty()) {
            for (final Token token : validUserTokens) {
                token.setExpired(true);
                token.setRevoke(true);
            }
            tokenRepository.saveAll(validUserTokens);
        }
    }
    
    /**
     * Refresca el token mediante su token de refresco.
     *
     * @param token el token
     * @return nuevo token
     */
    public TokenResponse refreshToken(String token) {
        if (! checkBearer(token)) {
            throw new IllegalArgumentException("Invalid Bearer token");
        }
        
        final String refreshToken = token.substring(7);
        final String userName = jwtService.extractUsername(token);
        
        if (userName == null) {
            throw new IllegalArgumentException("Ivalid Refresh Token");
        }
        
        final User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        
        if (! jwtService.isTokenValid(refreshToken, user)) {
            throw new IllegalArgumentException("Ivalid Refresh Token");
        }
        
        final String accesToken = jwtService.generateToken(user);
        
        revokeAllUserToken(user);
        saveUserToken(user, accesToken);
        
        return new TokenResponse(accesToken, refreshToken);
    }
}
