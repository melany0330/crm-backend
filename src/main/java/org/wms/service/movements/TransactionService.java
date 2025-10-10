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
package org.wms.service.movements;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wms.dto.movements.TransactionDto;
import org.wms.dto.movements.TransactionResponseDto;
import org.wms.model.movements.Transaction;
import org.wms.repository.inventory.InventoryRepository;
import org.wms.repository.purchases.PurchaseRepository;
import org.wms.repository.sale.SaleRepository;
import org.wms.repository.movements.TransactionRepository;
import org.wms.repository.movements.TypeMovementRepository;
import org.wms.repository.auth.UserRepository;

/**
 * @author wil
 */
@Service
@RequiredArgsConstructor
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;
    private final TypeMovementRepository movementRepository;
    private final PurchaseRepository purchaseRepository;
    private final SaleRepository saleRepository;
    
    public List<TransactionResponseDto> listAll() {
        return transactionRepository.findAll()
                .stream()
                .filter(Transaction::isStatus)
                .map((val) -> TransactionResponseDto.parse(val))
                .toList();
    }

    public Optional<TransactionResponseDto> listById(Long id) {
        return transactionRepository.findById(id)
                .map((val) -> TransactionResponseDto.parse(val));
    }

    public TransactionResponseDto create(TransactionDto model) {
        var inventory = inventoryRepository.findById(model.inventory())
                .orElseThrow();
        var user = userRepository.findById(model.user())
                .orElseThrow();
        var typeMovement = movementRepository.findById(model.typeMovement())
                .orElseThrow();
        var sale = saleRepository.findById(model.sale())
                .orElseThrow();
        var purchase = purchaseRepository.findById(((Number) model.purchase())
                .intValue())
                .orElseThrow();
        
        Transaction transaction = Transaction.builder()
                .amount(model.amount())
                .inventory(inventory)
                .movementDate(model.movementDate())
                .purchase(purchase)
                .reason(model.reason())
                .sale(sale)
                .status(true)
                .typeMovement(typeMovement)
                .user(user)
                .build();
        
        return TransactionResponseDto.parse(transactionRepository.save(transaction));
    }

    public TransactionResponseDto update(TransactionDto model, Long id) {
        var result = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        result.setAmount(model.amount());
        result.setReason(model.reason());

        return TransactionResponseDto.parse(transactionRepository.save(result));
    }
    
    public void deactivate(Long id) {
        var object = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        object.setStatus(false);
        transactionRepository.save(object);
    }
}
