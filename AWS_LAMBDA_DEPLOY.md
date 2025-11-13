# AWS Lambda Deployment Guide

## Requisitos Previos

1. **AWS CLI** instalado y configurado:
   ```bash
   aws configure
   ```

2. **AWS SAM CLI** instalado:
   ```bash
   brew install aws-sam-cli
   ```

3. **Java 17** instalado

4. **Gradle** configurado

## Configuración Inicial

1. **Copiar archivo de configuración:**
   ```bash
   cp samconfig.toml.example samconfig.toml
   ```

2. **Editar `samconfig.toml`** con tus valores:
   - Nombre del bucket S3
   - Credenciales de base de datos
   - Secret key para JWT
   - Hostname del cliente

3. **Si tu base de datos está en VPC**, añade los parámetros:
   ```toml
   "VpcId=vpc-xxxxx",
   "SubnetIds=subnet-xxx,subnet-yyy",
   "SecurityGroupIds=sg-xxxxx"
   ```

## Despliegue

### Opción 1: Script Automático
```bash
chmod +x deploy.sh
./deploy.sh
```

### Opción 2: Paso a Paso

1. **Compilar el proyecto:**
   ```bash
   ./gradlew clean build -x test
   ```

2. **Build con SAM:**
   ```bash
   sam build
   ```

3. **Desplegar:**
   ```bash
   sam deploy --guided  # Primera vez
   sam deploy           # Siguientes despliegues
   ```

## Testing Local

### Iniciar API localmente:
```bash
sam local start-api
```

### Invocar función directamente:
```bash
sam local invoke CrmBackendFunction -e events/test-event.json
```

### Ver logs en tiempo real:
```bash
sam logs -n CrmBackendFunction --stack-name crm-backend-stack --tail
```

## Consideraciones Importantes

### 1. **Conexión a Base de Datos**
- Si tu MariaDB está en AWS, considera usar **RDS Proxy** para manejar conexiones
- Lambda puede tener muchas instancias concurrentes, limita el pool de conexiones
- Configurado en `application.yml` perfil `lambda`: max 2 conexiones por instancia

### 2. **Cold Start**
- Primera ejecución puede tardar 5-10 segundos
- Usar **AWS Lambda SnapStart** para Java reduce esto a ~1 segundo
- Mantener la función "caliente" con EventBridge (opcional)

### 3. **Timeouts**
- Configurado en 30 segundos (puede aumentarse hasta 15 minutos)
- API Gateway tiene límite de 30 segundos para respuestas HTTP

### 4. **Tamaño del Paquete**
- Lambda tiene límite de 250 MB (unzipped)
- El bootJar actual debería estar dentro del límite

### 5. **Variables de Entorno**
- Todas las variables `WMS_*` deben configurarse en `template.yaml`
- Para secretos, considera usar AWS Secrets Manager o Parameter Store

## Monitoreo

### CloudWatch Logs
```bash
aws logs tail /aws/lambda/crm-backend-api --follow
```

### Métricas
- Invocations
- Duration
- Error rate
- Throttles

Accede a CloudWatch Metrics en la consola AWS.

## Costos Estimados

- **Lambda**: Primeros 1M requests/mes gratis, luego $0.20 por 1M
- **API Gateway**: $3.50 por millón de llamadas
- **CloudWatch Logs**: ~$0.50 por GB

Para tráfico bajo-medio, debería costar < $10/mes.

## Troubleshooting

### Error: "Task timed out after 30 seconds"
- Aumenta el timeout en `template.yaml`
- Verifica conexión a base de datos
- Revisa que RDS/MariaDB sea accesible desde Lambda

### Error: "Cannot connect to database"
- Verifica que Lambda esté en la misma VPC que la BD
- Verifica Security Groups permiten tráfico
- Verifica credenciales en variables de entorno

### Error: "Class not found"
- Verifica que el handler esté correctamente especificado
- Recompila: `./gradlew clean build`

### Cold start muy lento
- Considera habilitar Lambda SnapStart
- Reduce dependencias si es posible
- Aumenta memoria (más memoria = más CPU)

## Rollback

Si algo sale mal:
```bash
aws cloudformation delete-stack --stack-name crm-backend-stack
```

O volver a versión anterior:
```bash
sam deploy --parameter-overrides FunctionVersion=<version-anterior>
```

## Próximos Pasos

1. **CI/CD**: Integrar con GitHub Actions o AWS CodePipeline
2. **SnapStart**: Habilitar para reducir cold starts
3. **RDS Proxy**: Configurar para mejor manejo de conexiones
4. **Custom Domain**: Añadir dominio personalizado a API Gateway
5. **WAF**: Añadir protección con AWS WAF
