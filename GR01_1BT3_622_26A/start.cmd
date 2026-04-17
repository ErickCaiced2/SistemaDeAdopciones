@echo off
REM ========================================
REM   Sistema de Adopciones - Inicio
REM ========================================

cd /d "%~dp0"

echo.
echo ========================================
echo   SISTEMA DE ADOPCIONES
echo ========================================
echo.

REM ================================
REM Verificar Docker
REM ================================
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo WARNING: Docker no está instalado o no está en el PATH
    echo Instalar desde: https://www.docker.com/products/docker-desktop
    echo.
    pause
    exit /b
)

REM ================================
REM Iniciar Docker (MySQL)
REM ================================
echo Iniciando contenedores de Docker...
docker compose -f compose.yaml up -d

if %errorlevel% neq 0 (
    echo.
    echo ERROR al iniciar Docker. Asegúrate de que Docker Desktop esté corriendo.
    pause
    exit /b
)

REM ================================
REM Esperar base de datos
REM ================================
echo.
echo Esperando a que MySQL esté listo (10s)...
timeout /t 10 >nul

REM ================================
REM Crear carpeta faltante GraphQL
REM ================================
if not exist src\main\resources\graphql-client (
    echo.
    echo Creando carpeta faltante: graphql-client
    mkdir src\main\resources\graphql-client
)

REM ================================
REM Compilar proyecto
REM ================================
echo.
echo Compilando el proyecto...
call mvnw.cmd clean install

if %errorlevel% neq 0 (
    echo.
    echo ERROR en la compilación. Revisa los logs arriba.
    pause
    exit /b
)

REM ================================
REM Ejecutar aplicación
REM ================================
echo.
echo Ejecutando la aplicación...
echo.
echo ========================================
echo Sistema iniciado en: http://localhost:8080
echo ========================================
echo.

call mvnw.cmd spring-boot:run

pause
