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
package org.wms.handler;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.wms.Application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Handler de AWS Lambda para la aplicación Spring Boot.
 * Esta clase actúa como punto de entrada para las peticiones de API Gateway.
 *
 * @author wms
 */
public class StreamLambdaHandler implements RequestStreamHandler {
    
    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
    
    static {
        try {
            // Inicialización del handler de Spring Boot
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(Application.class);
            
            // Configuración para mejorar el rendimiento del cold start
            handler.activateSpringProfiles("lambda");
            
        } catch (ContainerInitializationException e) {
            // Si falla la inicialización, lanzar un error que será capturado por Lambda
            e.printStackTrace();
            throw new RuntimeException("No se pudo inicializar el handler de Spring Boot", e);
        }
    }

    /**
     * Maneja las peticiones entrantes de API Gateway y las procesa a través de Spring Boot.
     *
     * @param inputStream  Stream de entrada con la petición de API Gateway
     * @param outputStream Stream de salida para la respuesta
     * @param context      Contexto de ejecución de Lambda
     * @throws IOException Si ocurre un error al procesar los streams
     */
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) 
            throws IOException {
        handler.proxyStream(inputStream, outputStream, context);
    }
}
