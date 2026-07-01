@echo off
setlocal

set "JBR=C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.4\jbr"
set "JAVAC=%JBR%\bin\javac.exe"
set "JAVA=%JBR%\bin\java.exe"

if not exist "%JAVAC%" (
    echo Nao foi possivel encontrar o Java do IntelliJ em:
    echo %JBR%
    echo.
    echo Abra o projeto no IntelliJ e execute a classe api.ApiServer por la.
    pause
    exit /b 1
)

cd /d "%~dp0"

echo Compilando projeto...
"%JAVAC%" -d out -sourcepath src src\api\*.java src\model\*.java src\singleton\*.java src\observer\*.java src\adapter\*.java src\Main.java
if errorlevel 1 (
    echo Falha na compilacao.
    pause
    exit /b 1
)

echo.
echo Servidor iniciado. Abra no navegador:
echo http://localhost:8080
echo.
echo Pressione Ctrl+C para parar o servidor.
echo.

"%JAVA%" -cp out api.ApiServer
