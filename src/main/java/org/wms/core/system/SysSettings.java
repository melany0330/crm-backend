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

import java.util.function.Function;

/**
 * Esta clase permite configurar programáticamente la configuración de ejecución 
 * del microservicio {@code Gateway}. Es una alternativa al uso de las propiedades
 * del sistema.
 * 
 * @param <T> el tipo de dato
 * 
 * @author wil
 */
public class SysSettings<T> {    
    // *************************************************************************
    // métodos estáticos
    
    /** Deshabilita la documentación de swagger-ui. */
    public static final SysSettings<Boolean> DISABLE_SWAGGER_UI             = new SysSettings<>("WMS_DISABLE_SWAGGER_UI", StateInit.BOOLEAN);    
    /** Deshabilita la documentación de swagger (api-docs). */
    public static final SysSettings<Boolean> DISABLE_SWAGGER_API_DOCS       = new SysSettings<>("WMS_DISABLE_SWAGGER_API_DOCS", StateInit.BOOLEAN);    
    /** Determina la salida del depurador. */
    public static final SysSettings<String> DEBUG_STREAM                    = new SysSettings<>("WMS_DEBUG_STREAM", StateInit.STRING);    
    /**Habilita o deshabilita el depurador WMS. */
    public static final SysSettings<Boolean> DEBUG                          = new SysSettings<>("WMS_DEBUG", StateInit.BOOLEAN);
    
    /**
     * Interfaz encargada de gestionar los valores de cada configuración.
     *
     * @param <T> tipo de dato
     */
    private interface StateInit<T> extends Function<String, T> {
        // *************************************************************************
        // implementaciones
        
        /**
         * De tipo {@code boolean}
         */
        StateInit<Boolean> BOOLEAN = property -> {
            String value = System.getenv(property);
            return value == null ? null : Boolean.parseBoolean(value);
        };

        /**
         * De tipo {@code int}
         */
        StateInit<Integer> INT = Integer::getInteger;

        /**
         * De tipo {@code String}
         */
        StateInit<String> STRING = System::getProperty;
    }

    // *************************************************************************
    // variables
    
    /**
     * Propiedad o el nombre de la variables a buscar en el sistema.
     */
    private final String property;

    /**
     * Valor de al variable
     */
    private volatile T state;

    // *************************************************************************
    // contructores
    
    /**
     * Genera una nueva configuraciones con su tipo y variable
     *
     * @param property el nombre
     * @param init el tipo
     */
    SysSettings(String property, StateInit<? extends T> init) {
        this.property = property;
        this.state = init.apply(property);
    }

    /**
     * Devuelve la propiedad de la configuración
     * 
     * @return la propiedad
     */
    public String getProperty() {
        return property;
    }

    /**
     * Establece el valor de la opción.
     *
     * @param value el valor a establecer
     */
    public void set(T value) {
        this.state = value;
    }

    /**
     * Devuelve el valor de la opción.
     *
     * <p>Si no se ha establecido el valor de la opción, se devolverá nulo.</p>
     * @return valor-opción
     */
    public T get() {
        return state;
    }

    /**
     * Devuelve el valor de la opción.
     *
     * <p>Si no se ha establecido el valor de la opción, se devolverá el valor 
     * predeterminado especificado.</p>
     *
     * @param defaultValue el valor predeterminado
     * @return valor-opción
     */
    public T get(T defaultValue) {
        T nstate = this.state;
        if (nstate == null) {
            nstate = defaultValue;
        }
        return nstate;
    }
}
