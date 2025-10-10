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
package org.wms.config.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.wms.model.auth.Token;
import org.wms.model.auth.User;
import org.wms.repository.auth.TokenRepository;
import org.wms.repository.auth.UserRepository;
import org.wms.service.auth.JwtService;
import static org.wms.core.system.Checks.*;

/**
 * Clase encargado de aplicar un filtro a todas las peticiones mediante
 * HTTP/HTTPS.
 *
 * @author wil
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    
    /** Servicio JWT */
    private final JwtService jwService;    
     /** El servicio que gestiona los usuarios de spring. */
    private final UserDetailsService userDetailsService;    
     /** Servicio que gestiona los tokens. */
    private final TokenRepository tokenRepository;    
     /** Servicio que gestiona los usuarios. */
    private final UserRepository userRepository;

    /* (non-Javadoc)
     * OncePerRequestFilter#doFilterInternal
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (checkAuthPath(request.getServletPath())) {
            filterChain.doFilter(request, response);
            return;
        }
        
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (! checkBearer(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        final String jwtToken = authHeader.substring(7);
        final String userName = jwService.extractUsername(jwtToken);
        
        if (userName == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            return;
        }
        
        final Token token = tokenRepository.findByToken(jwtToken)
                .orElse(null);
        if (! checkTokenEpiration(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        final UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        final Optional<User> user = userRepository.findByUserName(userDetails.getUsername());
        if (user.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        
        final boolean isTokenValid = jwService.isTokenValid(jwtToken, user.get());
        if (! isTokenValid) {
            return;
        }
        
        final var authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        
        filterChain.doFilter(request, response);
    }

}
