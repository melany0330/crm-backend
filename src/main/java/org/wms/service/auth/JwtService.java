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

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.wms.model.auth.User;

/**
 * Clase encargada de gestionr el servicio de JWT
 *
 * @author wil
 */
@Service
public class JwtService {
    // *************************************************************************
    // variables
    /** Clave secreta para los tokens JWT. */
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    
    /** tiempo de expiración de tokens. */
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    /** tipo del token de refresco. */
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;
    
    // *************************************************************************
    // métodos
    
    /**
     * Extrace el nombre del usuario en el token dada.
     *
     * @param token el token
     *
     * @return el nombre del usuario
     */
    public String extractUsername(String token) {
        final Claims jwtToken = Jwts.parser()
                .verifyWith(getNewSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return  jwtToken.getSubject();
    }
    
    /**
     * Verifica si un tokeb es válido.
     *
     * @param token el token
     * @param user el usuario del token
     *
     * @return {@code true} si es válido, de lo contrario {@code false}
     */
    public boolean isTokenValid(String token, User user) {
        final String username = extractUsername(token);
        return Objects.equals(username, user.getName()) && !isTokenExpired(token);
    }
    
    /**
     * Verifica si el token ha expirado.
     *
     * @param token el token
     *
     * @return {@code true} si ha expirado, de lo contrario {@code false}
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    /**
     * Genera el token para el usuario dada.
     *
     * @param user el usuario
     *
     * @return un nuevo token
     */
    public String generateToken(User user) {
        return buildToken(user, jwtExpiration);
    }
    
    /**
     * Genera el token para refrescar el token de sesión
     *
     * @param user el usuaior
     *
     * @return un nuevo tokens
     */
    public String generateRefreshToken(User user) {
        return buildToken(user, refreshExpiration);
    }
    
    /**
     * Método encargado de construir el token de sesión del usuarios.
     * 
     * @param user el usuaiors
     * @param expiration tipo de expiración
     *
     * @return un nuevo token
     */
    private String buildToken(User user, long expiration) {
        return Jwts.builder()
                .id(Long.toBinaryString(user.getId()))
                .claims(Map.of("ri", Long.toBinaryString(user.getRol().getId())))
                .subject(user.getName())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getNewSecretKey())
                .compact();
    }
    
    /**
     * Extrace el nombre tipo de vigencia del token
     *
     * @param token el token
     *
     * @return el tipo
     */
    private Date extractExpiration(String token) {
        final Claims jwtToken = Jwts.parser()
                .verifyWith(getNewSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return  jwtToken.getExpiration();
    }
    
    /**
     * Gestiona la clave secreta para los tokens
     *
     * @return la clave
     */
    private SecretKey getNewSecretKey() {
        byte[] keyBytes= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
