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
package org.wms.service.auth;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wms.dto.auth.RoleDto;
import org.wms.dto.auth.UserDto;
import org.wms.model.auth.RegisterRequest;
import org.wms.model.auth.User;
import org.wms.repository.auth.RoleRepository;
import org.wms.repository.auth.UserRepository;

/**
 * @author wil
 */
@Service
@RequiredArgsConstructor
public class UserService {
   
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
    public List<UserDto> listAll() {
        return userRepository.findAll().stream()
                .filter(User::isStatus)
                .map((user) -> {
                    return UserDto.toUserDto(user);
                })
                .toList();
    }
    
    public Optional<UserDto> listById(Long id) {
        return userRepository.findById(id).map((user) -> {
            return UserDto.toUserDto(user);
        });
    }
    
    public UserDto update(RegisterRequest user, Long id) {
        var result = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Role not found"));
        var role = roleRepository.findById(user.role())
                                 .orElseThrow();

        result.setName(user.userName());
        result.setPassword(passwordEncoder.encode(user.password()));
        result.setRol(role);
        return UserDto.toUserDto(userRepository.save(result));
    }
    
    public void deactivate(Long id) {
        var role = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        role.setStatus(false);
        userRepository.save(role);
    }
}
