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
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wms.dto.auth.UserDto;
import org.wms.model.auth.RegisterRequest;
import org.wms.service.auth.UserService;

/**
 * @author wil
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "Routes that manage users")
public class UserController {
    
    private final UserService userService;
    
    @Operation(
            summary = "Gets all users",
            description = "Recover all active users"
    )
    @GetMapping("/list")
    public ResponseEntity<List<UserDto>> listAll() {
        var entity = userService.listAll();
        return ResponseEntity.ok(entity);
    }
    
    @Operation(
            summary = "Get a user by ID",
            description = "Recover all active users"
    )
    @GetMapping("/listById/{id}")
    public ResponseEntity<Optional<UserDto>> listById(@PathVariable Long id) {
        var entity = userService.listById(id);
        return ResponseEntity.ok(entity);
    }
    
    @Operation(
            summary = "Update user",
            description = "Update an existing user"
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<UserDto> update(@RequestBody RegisterRequest model, @PathVariable Long id) {
        var result = userService.update(model, id);
        return ResponseEntity.ok(result);
    }
    
    @Operation(
            summary = "Delete user",
            description = "Logically delete a user"
    )
    @DeleteMapping("/deactivate/{id}")
    public void deactivate(@PathVariable Long id) {
        userService.deactivate(id);
    }
}
