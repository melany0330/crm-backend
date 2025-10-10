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
import org.wms.dto.movements.TypeMovementDto;
import org.wms.model.movements.TypeMovement;
import org.wms.repository.movements.TypeMovementRepository;

/**
 * @author wil
 */
@Service
@RequiredArgsConstructor
public class TypeMovementService {
    
    private final TypeMovementRepository movementRepository;
    
    public List<TypeMovement> listAll() {
        return movementRepository.findAll().stream()
                .filter(TypeMovement::isStatus)
                .toList();
    }
    
    public Optional<TypeMovement> listById(Long id) {
        return movementRepository.findById(id);
    }
    
    public TypeMovement create(TypeMovementDto model) {
        var newRole = TypeMovement.builder()
                .name(model.name())
                .status(true)
                .build();
        return movementRepository.save(newRole);
    }
    
    public TypeMovement update(TypeMovementDto model, Long id) {
        var result = movementRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("TypeMovement not found"));
        result.setName(model.name());
        return movementRepository.save(result);
    }
    
    public void deactivate(Long id) {
        var role = movementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TypeMovement not found"));
        role.setStatus(false);
        movementRepository.save(role);
    }
}
