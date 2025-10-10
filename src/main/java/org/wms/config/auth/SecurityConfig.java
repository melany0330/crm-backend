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

import java.util.ArrayList;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import org.wms.core.system.SysSettings;
import org.wms.model.auth.Token;
import org.wms.repository.auth.TokenRepository;
import static org.wms.core.system.Checks.*;
import static org.wms.core.system.APIUtil.*;

/**
 * Clase encargada de gestionar las configuraciones de seguridad de spring.
 *
 * @author wil
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableConfigurationProperties(ApplicationProperties.class)
public class SecurityConfig {
    /** Filtro de autenticación*/
    private final JwtAuthFilter jwtAuthFilter;
    /** Proveedpr de autenficaciones (sesiones). */
    private final AuthenticationProvider authenticationProvider;
    /** Repositorio de tokens. */
    private final TokenRepository tokenRepository;
    /* Servicio de CORS */
    private final CorsComponent whitelist;

    private final ApplicationProperties applicationProperties;
    
    /**
     * Método encargado de aplicar los filtros para cada petición HTTP.
     *
     * @param http un objeto http
     * @return el filtro de seguridad
     *
     * @throws Exception si ocurre un error
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        var patterns = new ArrayList<>(applicationProperties.getPatterns());
        
        apiLog("Swagger");
        if (! SysSettings.DISABLE_SWAGGER_UI.get(true)) {
            patterns.add("/swagger-ui/**");
            apiLogMore(" * UI enabled");
        }        
        if (! SysSettings.DISABLE_SWAGGER_API_DOCS.get(true)) {
            patterns.add("/api-docs/**");
            apiLogMore(" * API-DOCS enabled");
        }
        if (SysSettings.DEBUG.get(false)) {
            apiLog("Public routes:");
            for (String path : patterns) {
                apiLogMore(" *   " + path);
            }
        }
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> {
                    httpSecurityCorsConfigurer.configurationSource(websiteConfigurationSource());
                })
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(
                            patterns.toArray(String[]::new)
                    )
                            .permitAll()
                            .anyRequest()
                            .authenticated();
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> {
                    logout.logoutUrl("/api/auth/logout")
                            .addLogoutHandler((request, response, authentication) -> {
                                final var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
                                logout(authHeader);
                            })
                            .logoutSuccessHandler((request, response, authentication) ->
                                    SecurityContextHolder.clearContext());
                });
                
        return http.build();
    }
    
    private UrlBasedCorsConfigurationSource websiteConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                whitelist.getHost()
        ));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowedMethods(whitelist.getMethods());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        if (SysSettings.DEBUG.get(false)) {
            apiLog("Client allowed (CORS):");
            apiLogMore(" * Host:    " + whitelist.getHost());
            apiLogMore(" * Methods: "  +whitelist.getMethods().toString());
        }        
        return source;
    }
    
    /**
     * Método encargado de cerrar una sesión activa.
     *
     * @param token el token de la sesión
     */
    private void logout(String token) {
        if (! checkBearer(token)) {
            throw new IllegalArgumentException("Ivalid Token");
        }

        final String jwtToken = token.substring(7);
        final Token foundToken = tokenRepository.findByToken(jwtToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Token"));
        foundToken.setExpired(true);
        foundToken.setRevoke(true);
        tokenRepository.save(foundToken);
    }
}
