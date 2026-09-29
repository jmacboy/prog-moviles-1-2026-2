@echo off
setlocal EnableExtensions

REM ============================================================
REM ENTREGA PERIODICA - EXAMEN (Android - Windows)
REM Crea y envia un ZIP nuevo cada cierto tiempo.
REM Excluye lo que un proyecto Android ignora en git (build\,
REM .gradle\, .idea\, local.properties, *.iml, *.apk, *.aab, etc.)
REM y tambien los ZIPs/scripts sueltos.
REM ============================================================

set "PROJECT_DIR=%~dp0"
set "PROJECT_DIR=%PROJECT_DIR:~0,-1%"
set "SERVER_IP=192.168.70.166"
set "SERVER_PORT=3000"
set "INTERVAL_MINUTES=10"

echo.
echo ==================================================
echo      ENTREGA PERIODICA DE EXAMEN (Android)
echo ==================================================
echo.

set /p "STUDENT_NAME=Nombre completo: "
set /p "STUDENT_CODE=Codigo de estudiante: "

if "%STUDENT_NAME%"=="" (
    echo.
    echo Error: debes ingresar tu nombre.
    pause
    exit /b 1
)

if "%STUDENT_CODE%"=="" (
    echo.
    echo Error: debes ingresar tu codigo.
    pause
    exit /b 1
)

set /a WAIT_SECONDS=INTERVAL_MINUTES*60

REM ============================================================
REM LOCALIZAR POWERSHELL
REM Algunas PCs no tienen powershell.exe en el PATH. Usamos la ruta
REM fija del sistema como respaldo si "where" no lo encuentra.
REM ============================================================
set "PS_EXE=powershell"
where powershell >nul 2>&1
if errorlevel 1 set "PS_EXE=%SystemRoot%\System32\WindowsPowerShell\v1.0\powershell.exe"

if not exist "%PS_EXE%" (
    if /i not "%PS_EXE%"=="powershell" (
        echo.
        echo ERROR: No se encontro PowerShell en esta PC.
        pause
        exit /b 1
    )
)

:LOOP
echo.
echo --------------------------------------------------
echo Preparando nueva entrega...
echo --------------------------------------------------

for /f "tokens=1-3 delims=/ " %%a in ("%date%") do set "TODAY=%%c-%%b-%%a"
for /f "tokens=1-3 delims=:., " %%a in ("%time%") do set "CLOCK=%%a-%%b-%%c"
set "NOW=%TODAY%_%CLOCK%"

set "SAFE_NAME=%STUDENT_NAME: =_%"
set "STAGE_DIR=%TEMP%\examen_stage_%RANDOM%"
set "TEMP_ZIP=%TEMP%\examen_%STUDENT_CODE%_%RANDOM%.zip"
set "FILENAME=%STUDENT_CODE%_%SAFE_NAME%_%NOW%.zip"

echo Nombre:   %STUDENT_NAME%
echo Codigo:   %STUDENT_CODE%
echo Fecha:    %NOW%
echo.

echo Preparando archivos del proyecto...

REM robocopy excluye por nombre en cualquier nivel de carpeta, a
REM diferencia de un simple filtro del primer nivel: asi se dejan
REM afuera los build/ y .gradle/ de cada modulo, no solo los de la raiz.
robocopy "%PROJECT_DIR%" "%STAGE_DIR%" /E /NFL /NDL /NJH /NJS ^
  /XD ".git" "build" ".gradle" ".idea" ".externalNativeBuild" ".cxx" "captures" ^
  /XF "local.properties" "*.iml" "*.apk" "*.aab" "*.hprof" ".DS_Store" "*.zip" "*.bat"

if %ERRORLEVEL% GEQ 8 (
    echo.
    echo ERROR: No se pudo preparar los archivos del proyecto.
    rmdir /s /q "%STAGE_DIR%" >nul 2>&1
    goto WAIT
)

echo Creando ZIP nuevo...

"%PS_EXE%" -NoProfile -ExecutionPolicy Bypass -Command ^
  "$src = '%STAGE_DIR%';" ^
  "$zip = '%TEMP_ZIP%';" ^
  "$items = Get-ChildItem -LiteralPath $src -Force;" ^
  "Compress-Archive -Path $items.FullName -DestinationPath $zip -Force"

if errorlevel 1 (
    echo.
    echo ERROR: No se pudo crear el ZIP.
    rmdir /s /q "%STAGE_DIR%" >nul 2>&1
    goto WAIT
)

rmdir /s /q "%STAGE_DIR%" >nul 2>&1

echo ZIP creado. Enviando al servidor...

"%PS_EXE%" -NoProfile -ExecutionPolicy Bypass -Command ^
  "$zipBytes = [System.IO.File]::ReadAllBytes('%TEMP_ZIP%');" ^
  "$base64 = [Convert]::ToBase64String($zipBytes);" ^
  "$body = @{ studentName='%STUDENT_NAME%'; studentCode='%STUDENT_CODE%'; filename='%FILENAME%'; zipBase64=$base64; timestamp='%NOW%' } | ConvertTo-Json -Compress;" ^
  "try { $response = Invoke-RestMethod -Uri 'http://%SERVER_IP%:%SERVER_PORT%/submit' -Method Post -ContentType 'application/json' -Body $body; Write-Host 'Entrega recibida correctamente.' } catch { Write-Host 'ERROR: No se pudo enviar la entrega.'; Write-Host $_.Exception.Message; }"

del /q "%TEMP_ZIP%" >nul 2>&1

:WAIT
echo.
echo Proxima entrega en %INTERVAL_MINUTES% minutos...
timeout /t %WAIT_SECONDS% /nobreak >nul
goto LOOP
