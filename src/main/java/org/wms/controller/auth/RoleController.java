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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wms.dto.auth.RoleDto;
import org.wms.model.auth.Role;
import org.wms.service.auth.RoleService;

/**
 * @author wil
 */
@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
@Tag(name = "Role", description = "Routes that manage roles")
public class RoleController {
    
    private final RoleService roleService;
    
    @Operation(
            summary = "Gets all roles",
            description = "Recover all active roles"
    )
    @GetMapping("/list")
    public ResponseEntity<List<Role>> listAll() {
        var roles = roleService.listAll();
        return ResponseEntity.ok(roles);
    }
    
    @Operation(
            summary = "Get a role by ID",
            description = "Recover all active roles"
    )
    @GetMapping("/listById/{id}")
    public ResponseEntity<Optional<Role>> listById(@PathVariable Long id) {
        var role = roleService.listById(id);
        return ResponseEntity.ok(role);
    }
    
    @Operation(
            summary = "Create role",
            description = "Create a new role"
    )
    @PostMapping("/create")
    public ResponseEntity<Role> create(@RequestBody RoleDto role) {
        var result = roleService.create(role);
        return ResponseEntity.ok(result);
    }
    
    @Operation(
            summary = "Update role",
            description = "Update an existing role"
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<Role> update(@RequestBody RoleDto role, @PathVariable Long id) {
        var result = roleService.update(role, id);
        return ResponseEntity.ok(result);
    }
    
    @Operation(
            summary = "Delete role",
            description = "Logically delete a role"
    )
    @DeleteMapping("/deactivate/{id}")
    public void deactivate(@PathVariable Long id) {
        roleService.deactivate(id);
    }
}
