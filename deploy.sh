#!/bin/bash

# Script de despliegue para AWS Lambda usando SAM

set -e

echo "🚀 Iniciando proceso de despliegue a AWS Lambda..."

# Verificar que AWS CLI está instalado
if ! command -v aws &> /dev/null; then
    echo "❌ AWS CLI no está instalado. Por favor instálalo primero."
    exit 1
fi

# Verificar que SAM CLI está instalado
if ! command -v sam &> /dev/null; then
    echo "❌ SAM CLI no está instalado. Por favor instálalo primero."
    echo "Instalar con: brew install aws-sam-cli"
    exit 1
fi

# Verificar que existe samconfig.toml
if [ ! -f "samconfig.toml" ]; then
    echo "❌ No se encontró samconfig.toml"
    echo "Copia samconfig.toml.example y configura tus valores:"
    echo "  cp samconfig.toml.example samconfig.toml"
    exit 1
fi

echo "📦 Compilando el proyecto..."
./gradlew clean build -x test

echo "🏗️  Construyendo con SAM..."
sam build

echo "🚀 Desplegando a AWS..."
sam deploy

echo "✅ Despliegue completado!"
echo ""
echo "Para ver los logs:"
echo "  sam logs -n CrmBackendFunction --stack-name crm-backend-stack --tail"
echo ""
echo "Para probar localmente:"
echo "  sam local start-api"
