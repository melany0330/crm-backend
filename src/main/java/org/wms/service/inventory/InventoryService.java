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
package org.wms.service.inventory;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wms.dto.inventory.InventoryDto;
import org.wms.model.inventory.Inventory;
import org.wms.repository.inventory.InventoryRepository;
import org.wms.repository.movements.ProductRepository;

/**
 * @author wil
 */
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public List<InventoryDto> listAll() {
        return inventoryRepository.findAll().stream()
                .filter(Inventory::isStatus)
                .map((val) -> InventoryDto.parse(val))
                .toList();
    }

    public Optional<InventoryDto> listById(Long id) {
        return inventoryRepository.findById(id)
                .map((val) -> InventoryDto.parse(val) );
    }

    public InventoryDto create(InventoryDto model) {
        Number idProduct = model.product();
        var product = productRepository.findById(idProduct.intValue())
                .orElseThrow();

        var entity = Inventory.builder()
                .batch(model.batch())
                .currentQuantity(model.currentQuantity())
                .expirationDate(model.expirationDate())
                .product(product)
                .status(true)
                .build();
        return InventoryDto.parse(inventoryRepository.save(entity));
    }

    public InventoryDto update(InventoryDto model, Long id) {
        var result = inventoryRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Inventory not found"));
        Number idProduct = model.product();
        var product = productRepository.findById(idProduct.intValue())
                .orElseThrow();

        result.setBatch(model.batch());
        result.setCurrentQuantity(model.currentQuantity());
        result.setExpirationDate(model.expirationDate());
        result.setProduct(product);
        return InventoryDto.parse(inventoryRepository.save(result));
    }

    public void deactivate(Long id) {
        var role = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
        role.setStatus(false);
        inventoryRepository.save(role);
    }
}
