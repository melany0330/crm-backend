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
package org.wms.controller.inventory;

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
import org.wms.dto.inventory.InventoryDto;
import org.wms.model.inventory.Inventory;
import org.wms.service.inventory.InventoryService;

/**
 * @author wil
 */
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Routes that manage inventories")
public class InventoryController {
    
    private final InventoryService inventoryService;
    
    @Operation(
            summary = "Gets all inventories",
            description = "Recover all active inventories"
    )
    @GetMapping("/list")
    public ResponseEntity<List<InventoryDto>> listAll() {
        var result = inventoryService.listAll();
        return ResponseEntity.ok(result);
    }
    

    @Operation(
            summary = "Get a inventory by ID",
            description = "Recover all active inventories"
    )
    @GetMapping("/listById/{id}")
    public ResponseEntity<Optional<InventoryDto>> listById(@PathVariable Long id) {
        var result = inventoryService.listById(id);
        return ResponseEntity.ok(result);
    }
    
    @Operation(
            summary = "Create inventory",
            description = "Create a new inventory"
    )
    @PostMapping("/create")
    public ResponseEntity<InventoryDto> create(@RequestBody InventoryDto role) {
        var result = inventoryService.create(role);
        return ResponseEntity.ok(result);
    }
    
    @Operation(
            summary = "Update inventory",
            description = "Update an existing inventory"
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<InventoryDto> update(@RequestBody InventoryDto role, @PathVariable Long id) {
        var result = inventoryService.update(role, id);
        return ResponseEntity.ok(result);
    }
    
    @Operation(
            summary = "Delete inventory",
            description = "Logically delete a inventory"
    )
    @DeleteMapping("/deactivate/{id}")
    public void deactivate(@PathVariable Long id) {
        inventoryService.deactivate(id);
    }
}
