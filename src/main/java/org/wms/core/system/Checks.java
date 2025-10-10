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
package org.wms.core.system;

import java.util.Objects;
import org.wms.model.auth.Token;

/**
 * Clase encargada de implementar métodos de verificaciones.
 *
 * @author wil
 */
public final class Checks {
    
    // *************************************************************************
    // constantes
    public static final Boolean DEBUG = SysSettings.DEBUG.get(false);
    
    // *************************************************************************
    // métodos estáticas
    
    /**
     * Verifica si el token es válido.
     *
     * @param token el token a verificar
     *
     * @return {@code true} si aun es válido, de lo contrario {@code false} si
     * ha expirdado.
     */
    public static boolean checkTokenEpiration(Token token) {
        if (token == null || token.isExpired() || token.isRevoke()) {
            return false;
        }
        return true;
    }
    
    /**
     * Verifica si la ruta pertenece a un endpoint de autenticación
     *
     * @param path la ruta a verificar
     *
     * @return {@code true} si es válido, de lo contrario {@code false} no 
     * corresponde
     */
    public static boolean checkAuthPath(String path) {
        if (Objects.requireNonNull(path).contains("/api/auth")) {
            return true;
        }
        return false;
    }
    
    /**
     * Verifica si el token es de tipo {@code Bearer}
     *
     * @param token el token a verificar
     *
     * @return {@code true} si aun es válido, de lo contrario {@code false} si
     * es de otro tipo
     */
    public static boolean checkBearer(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return false;
        }
        return true;
    }
}
