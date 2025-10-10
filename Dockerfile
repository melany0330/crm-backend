# Usa una imagen base del OpenJDK 21
FROM openjdk:21-jdk-slim

# Crear un usuario para ejecutar la app
RUN addgroup --system spring && adduser --system --ingroup spring spring

# Crear directorio de uploads
RUN mkdir -p /app/uploads && chown -R spring:spring /app/uploads

# Ejecutar como usuario
USER spring:spring

# Copiar el jar ya compilado
COPY build/libs/grupo5-backend-0.0.1-boot.jar /app.jar

# Comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "/app.jar"]