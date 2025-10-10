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
package org.wms.controller.movements;

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
import org.wms.dto.movements.TransactionDto;
import org.wms.dto.movements.TransactionResponseDto;
import org.wms.service.movements.TransactionService;

/**
 * @author wil
 */
@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
@Tag(name = "Transaction", description = "Rutas que gestionan transacciones")
public class TransactionController {
    
    private final TransactionService transactionService;
    
    @Operation(
            summary = "Gets all transacciones",
            description = "Recover all active transacciones"
    )
    @GetMapping("/list")
    public ResponseEntity<List<TransactionResponseDto>> listAll() {
        var result = transactionService.listAll();
        return ResponseEntity.ok(result);
    }
    
    @Operation(
            summary = "Get a transaccione by ID",
            description = "Recover all active transacciones"
    )
    @GetMapping("/listById/{id}")
    public ResponseEntity<Optional<TransactionResponseDto>> listById(@PathVariable Long id) {
        var result = transactionService.listById(id);
        return ResponseEntity.ok(result);
    }
    
    @Operation(
            summary = "Create transaccione",
            description = "Create a new transaccione"
    )
    @PostMapping("/create")
    public ResponseEntity<TransactionResponseDto> create(@RequestBody TransactionDto role) {
        var result = transactionService.create(role);
        return ResponseEntity.ok(result);
    }
    
    @Operation(
            summary = "Update transaccione",
            description = "Update an existing transaccione"
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<TransactionResponseDto> update(@RequestBody TransactionDto role, @PathVariable Long id) {
        var result = transactionService.update(role, id);
        return ResponseEntity.ok(result);
    }
    
    @Operation(
            summary = "Delete transaccione",
            description = "Logically delete a transaccione"
    )
    @DeleteMapping("/deactivate/{id}")
    public void deactivate(@PathVariable Long id) {
        transactionService.deactivate(id);
    }
}
