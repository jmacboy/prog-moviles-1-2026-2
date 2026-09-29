#!/bin/bash
set -u

# ==========================================================
#             ENTREGA MANUAL INMEDIATA (Android - macOS)
# ==========================================================

# IP de la PC del profesor
SERVER_IP="192.168.70.166"

# Puerto del servidor
SERVER_PORT="3000"


# ==========================================================
#             CARPETA DEL PROYECTO
# ==========================================================

# La carpeta donde esta este script es la raiz del proyecto Android.
PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"


# ==========================================================
#             DATOS DEL ESTUDIANTE
# ==========================================================

echo ""
echo "=================================================="
echo "            ENTREGA DEL EXAMEN (Android)"
echo "=================================================="
echo ""

echo "Proyecto:"
echo "$PROJECT_DIR"
echo ""

read -r -p "Nombre completo: " STUDENT_NAME
read -r -p "Codigo de estudiante: " STUDENT_CODE

if [ -z "$STUDENT_NAME" ]; then
    echo ""
    echo "ERROR: Debes ingresar tu nombre."
    read -r -p "Presiona Enter para salir..." _
    exit 1
fi

if [ -z "$STUDENT_CODE" ]; then
    echo ""
    echo "ERROR: Debes ingresar tu codigo."
    read -r -p "Presiona Enter para salir..." _
    exit 1
fi


# ==========================================================
#             VERIFICAR HERRAMIENTAS
# ==========================================================

if ! command -v zip >/dev/null 2>&1; then
    echo ""
    echo "ERROR: No se encontro el comando 'zip' en esta Mac."
    read -r -p "Presiona Enter para salir..." _
    exit 1
fi

if ! command -v rsync >/dev/null 2>&1; then
    echo ""
    echo "ERROR: No se encontro el comando 'rsync' en esta Mac."
    read -r -p "Presiona Enter para salir..." _
    exit 1
fi

if ! command -v curl >/dev/null 2>&1; then
    echo ""
    echo "ERROR: No se encontro el comando 'curl' en esta Mac."
    read -r -p "Presiona Enter para salir..." _
    exit 1
fi


# ==========================================================
#             COPIAR SOLO LO NECESARIO (sin lo gitignoreado)
# ==========================================================

STAGE_DIR="$(mktemp -d -t examen_stage)"
TEMP_ZIP="$(mktemp -t examen).zip"
rm -f "$TEMP_ZIP" 2>/dev/null

echo ""
echo "[$(date '+%H:%M:%S')] Preparando archivos del proyecto..."
echo ""

# Excluye lo que un proyecto Android ignora en git: build outputs,
# cachés de Gradle/IDE, configuracion local de SDK, y binarios generados.
rsync -a \
    --exclude ".git/" \
    --exclude "build/" \
    --exclude ".gradle/" \
    --exclude ".idea/" \
    --exclude ".externalNativeBuild/" \
    --exclude ".cxx/" \
    --exclude "captures/" \
    --exclude "local.properties" \
    --exclude "*.iml" \
    --exclude "*.apk" \
    --exclude "*.aab" \
    --exclude "*.hprof" \
    --exclude ".DS_Store" \
    "$PROJECT_DIR"/ "$STAGE_DIR"/

if [ $? -ne 0 ]; then
    echo ""
    echo "ERROR: No se pudo preparar los archivos del proyecto."
    rm -rf "$STAGE_DIR" 2>/dev/null
    read -r -p "Presiona Enter para salir..." _
    exit 1
fi


# ==========================================================
#             CREAR ZIP TEMPORAL
# ==========================================================

echo "[$(date '+%H:%M:%S')] Comprimiendo proyecto..."
echo ""

(cd "$STAGE_DIR" && zip -rq "$TEMP_ZIP" .)

if [ $? -ne 0 ] || [ ! -f "$TEMP_ZIP" ]; then
    echo ""
    echo "ERROR: No se pudo crear el ZIP."
    rm -rf "$STAGE_DIR" 2>/dev/null
    read -r -p "Presiona Enter para salir..." _
    exit 1
fi

rm -rf "$STAGE_DIR" 2>/dev/null


# ==========================================================
#             ENVIAR AL SERVIDOR
# ==========================================================

echo ""
echo "[$(date '+%H:%M:%S')] Enviando al servidor..."
echo ""

json_escape() {
    printf '%s' "$1" | sed 's/\\/\\\\/g; s/"/\\"/g'
}

ZIP_B64="$(base64 < "$TEMP_ZIP" | tr -d '\n')"
NAME_ESC="$(json_escape "$STUDENT_NAME")"
CODE_ESC="$(json_escape "$STUDENT_CODE")"
FILENAME="$(basename "$TEMP_ZIP")"
TIMESTAMP="$(date -u +%Y-%m-%dT%H:%M:%SZ)"

BODY="$(printf '{"studentName":"%s","studentCode":"%s","filename":"%s","zipBase64":"%s","timestamp":"%s"}' \
    "$NAME_ESC" "$CODE_ESC" "$FILENAME" "$ZIP_B64" "$TIMESTAMP")"

RESPONSE="$(curl -s -m 30 -X POST "http://$SERVER_IP:$SERVER_PORT/submit" \
    -H "Content-Type: application/json" \
    -d "$BODY")"
CURL_RESULT=$?

rm -f "$TEMP_ZIP" 2>/dev/null


# ==========================================================
#             RESULTADO
# ==========================================================

echo ""

if [ "$CURL_RESULT" -eq 0 ] && printf '%s' "$RESPONSE" | grep -q '"ok":true'; then
    SAVED="$(printf '%s' "$RESPONSE" | grep -o '"savedFilename":"[^"]*"' | cut -d'"' -f4)"
    SHA="$(printf '%s' "$RESPONSE" | grep -o '"sha256":"[^"]*"' | cut -d'"' -f4)"
    echo "=================================================="
    echo "      ENTREGA RECIBIDA CORRECTAMENTE"
    echo "=================================================="
    echo "OK: $SAVED"
    echo "SHA-256: $SHA"
    RESULT=0
else
    echo "=================================================="
    echo "      ERROR: LA ENTREGA NO FUE RECIBIDA"
    echo "=================================================="
    [ -n "${RESPONSE:-}" ] && echo "$RESPONSE"
    RESULT=1
fi

echo ""
read -r -p "Presiona Enter para salir..." _
exit $RESULT
