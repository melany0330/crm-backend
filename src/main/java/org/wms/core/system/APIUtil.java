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

import java.io.PrintStream;
import java.util.function.Supplier;
import static org.wms.core.system.Checks.*;

/**
 * Clase encargada de ofrecer métodos de utilidades.
 *
 * @author wil
 */
public final class APIUtil {
    // *************************************************************************
    // constantes

    /**
     * El {@link PrintStream} que WMS utiliza para imprimir información de depuración
     * y errores no fatales. El valor predeterminado es {@link System#err}, que se
     * puede modificar con {@link SysSettings#DEBUG_STREAM}.
     */
    public static final PrintStream DEBUG_STREAM = getDebugStream();

    // *************************************************************************
    // métodos estáticos
   
    /**
     * Método encargado de crear una nueva salida para los 'logs'.
     *
     * @return salida del depurador
     */
    @SuppressWarnings({"unchecked", "UseOfSystemOutOrSystemErr"})
    private static PrintStream getDebugStream() {
        PrintStream debugStream = System.err;

        Object state = SysSettings.DEBUG_STREAM.get();
        if (state instanceof String) {
            try {
                Supplier<PrintStream> factory = (Supplier<PrintStream>)Class.forName((String)state)
                    .getDeclaredConstructor()
                    .newInstance();
                debugStream = factory.get();
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        } else if (state instanceof Supplier<?>) {
            debugStream = ((Supplier<PrintStream>)state).get();
        } else if (state instanceof PrintStream) {
            debugStream = (PrintStream)state;
        }

        return debugStream;
    }
 
    /**
     * Imprime el mensaje especificado en {@link #DEBUG_STREAM} si {@link Checks#DEBUG} es verdadero.
     *
     * @param msg el mensaje a imprimir
     */
    public static void apiLog(CharSequence msg) {
        if (DEBUG) {
            DEBUG_STREAM.print("[WMS] " + msg + "\n");
        }
    }

    /**
     * Igual que {@link #apiLog}, pero reemplaza el prefijo WMS con un carácter de tabulación.
     *
     * @param msg el mensaje a imprimir, en continuación de un mensaje anterior
     */
    public static void apiLogMore(CharSequence msg) {
        if (DEBUG) {
            DEBUG_STREAM.print("\t" + msg + "\n");
        }
    }
}
