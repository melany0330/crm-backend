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
package org.wms.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.HttpHeaders.*;

import org.wms.model.auth.LoginRequest;
import org.wms.model.auth.RegisterRequest;
import org.wms.model.auth.TokenResponse;
import org.wms.service.auth.AuthService;

/**
 * Clase encargada de gestionar las rutas HTTP para las sesiones.
 *
 * @author wil
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Routes that manage user authentication")
public class AuthController {
    // *************************************************************************
    // servicios
    
    /** Servicio de autenticación. */
    private final AuthService service;

    // *************************************************************************
    // métodos http

    @Operation(summary = "Register a new user and generate their token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Create the new user",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponse.class))}),
        @ApiResponse(responseCode = "403", description = "The user already exists",
                content = @Content
        )})
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody RegisterRequest request) {
        final TokenResponse token = service.register(request);
        return ResponseEntity.ok(token);
    }
    
    @Operation(summary = "A user logs into the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "authenticated successfully",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponse.class))}),
        @ApiResponse(responseCode = "403", description = "It does not exist in the system",
                content = @Content
        )})
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody LoginRequest request) {
        final TokenResponse token = service.login(request);
        return ResponseEntity.ok(token);
    }
    
    @Operation(summary = "Update the authentication token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successful update",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponse.class))}),
        @ApiResponse(responseCode = "403", description = "Error with authentication tokens",
                content = @Content
        )})
    @PostMapping("/refresh")
    public TokenResponse refreshToken(@RequestHeader(AUTHORIZATION) String authHeader) {
        return service.refreshToken(authHeader);
    }
}
