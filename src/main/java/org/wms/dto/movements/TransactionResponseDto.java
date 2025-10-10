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
package org.wms.dto.movements;

import java.util.Date;
import org.wms.dto.auth.UserDto;
import org.wms.dto.inventory.InventoryDto;
import org.wms.dto.purchases.PurchaseDto;
import org.wms.dto.sale.SaleDto;
import org.wms.model.movements.Transaction;

/**
 * @author wil
 */
public record TransactionResponseDto(
        Long id,
        InventoryDto inventory,
        int amount,
        Date movementDate,
        String reason,
        UserDto user,
        TypeMovementDto typeMovement,
        SaleDto sale,
        PurchaseDto purchase,
        boolean status
        ) {

    public static TransactionResponseDto parse(Transaction table) {
        var inventory   = table.getInventory();
        var user        = table.getUser();
        var typemov     = table.getTypeMovement();
        var sale        = table.getSale();
        var purchase    = table.getPurchase();
        return new TransactionResponseDto(
                table.getId(),
                InventoryDto.parse(inventory),
                table.getAmount(),
                table.getMovementDate(),
                table.getReason(),
                UserDto.toUserDto(user), 
                new TypeMovementDto(typemov.getId(), typemov.getName(), typemov.isStatus()),
                new SaleDto(sale), 
                new PurchaseDto(purchase),
                table.isStatus());
    }
}
