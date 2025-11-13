# 🚀 Cambios Realizados para AWS Lambda

## ✅ Archivos Modificados

1. **`build.gradle`**
   - ✅ Añadidas dependencias de AWS Lambda
   - ✅ Configurada tarea `buildZip` para empaquetado

2. **`gradle.properties`**
   - ✅ Cambiado a Java 17 (requerido por AWS Lambda)

3. **`src/main/java/org/wms/handler/StreamLambdaHandler.java`**
   - ✅ Creado handler de Lambda
   - ✅ Integración con Spring Boot Container

4. **`src/main/resources/application.yml`**
   - ✅ Añadido perfil `lambda` con configuración optimizada

## ✅ Archivos Nuevos

1. **`template.yaml`**
   - Configuración de AWS SAM para despliegue
   - Define la función Lambda y API Gateway
   - Variables de entorno configurables

2. **`samconfig.toml.example`**
   - Ejemplo de configuración para SAM CLI
   - Parámetros de despliegue

3. **`deploy.sh`**
   - Script automatizado de despliegue
   - Verifica requisitos y ejecuta el deployment

4. **`AWS_LAMBDA_DEPLOY.md`**
   - Guía completa de despliegue
   - Troubleshooting y mejores prácticas

5. **`events/test-event.json`**
   - Evento de prueba para testing local

## 📋 Próximos Pasos

### 1. Instalar Herramientas AWS (si no las tienes)

```bash
# AWS CLI
brew install awscli
aws configure

# AWS SAM CLI
brew install aws-sam-cli
```

### 2. Configurar Parámetros

```bash
# Copiar archivo de ejemplo
cp samconfig.toml.example samconfig.toml

# Editar con tus valores
# - Bucket S3 para deployment
# - Credenciales de base de datos
# - JWT secret key
# - Hostname del cliente
```

### 3. Probar Localmente (Opcional)

```bash
# Iniciar API localmente
sam local start-api

# Probar endpoint
curl http://localhost:3000/api/auth/login
```

### 4. Desplegar a AWS

```bash
# Opción 1: Script automatizado
./deploy.sh

# Opción 2: Manual
./gradlew clean build -x test
sam build
sam deploy --guided  # Primera vez
```

### 5. Verificar Despliegue

Después del despliegue, obtendrás una URL como:
```
https://xxxxxxxx.execute-api.us-east-1.amazonaws.com/prod/
```

Prueba tu API:
```bash
curl https://tu-api-url.amazonaws.com/prod/api/auth/login
```

## ⚠️ Consideraciones Importantes

### Base de Datos
- Si tu MariaDB está en AWS:
  - Configura VPC, Subnets y Security Groups en `template.yaml`
  - Considera usar RDS Proxy para manejar conexiones

- Si tu MariaDB está on-premise:
  - Necesitarás VPN o Direct Connect
  - O migrar la BD a AWS RDS

### Variables de Entorno
Todas las variables `WMS_*` deben configurarse en `template.yaml` sección `Parameters`.

### Costos
- Lambda: ~$0.20 por millón de requests (primeros 1M gratis/mes)
- API Gateway: ~$3.50 por millón de requests
- Estimado: < $10/mes para tráfico bajo-medio

### Performance
- Cold Start: 5-10 segundos primera vez
- Solicitudes subsecuentes: < 1 segundo
- Considera Lambda SnapStart para mejorar cold starts

## 🔍 Testing y Monitoreo

```bash
# Ver logs en tiempo real
sam logs -n CrmBackendFunction --stack-name crm-backend-stack --tail

# O con AWS CLI
aws logs tail /aws/lambda/crm-backend-api --follow

# Ver métricas en CloudWatch
# https://console.aws.amazon.com/cloudwatch/
```

## 🆘 Problemas Comunes

Ver `AWS_LAMBDA_DEPLOY.md` para guía completa de troubleshooting.

### Quick Fixes:

**Error de timeout:**
```yaml
# En template.yaml, aumentar:
Timeout: 60  # segundos
```

**Error de conexión a BD:**
- Verificar Security Groups
- Verificar que Lambda esté en la misma VPC que la BD
- Verificar credenciales en variables de entorno

**Error de memoria:**
```yaml
# En template.yaml, aumentar:
MemorySize: 3008  # MB (más memoria = más CPU)
```

## 📚 Recursos Adicionales

- [AWS Lambda Documentation](https://docs.aws.amazon.com/lambda/)
- [AWS SAM Documentation](https://docs.aws.amazon.com/serverless-application-model/)
- [Spring Boot on Lambda](https://github.com/awslabs/aws-serverless-java-container)

---

**Nota:** El proyecto sigue funcionando normalmente como aplicación Spring Boot tradicional. Los cambios son compatibles con ambos modos de ejecución (servidor tradicional y Lambda).
