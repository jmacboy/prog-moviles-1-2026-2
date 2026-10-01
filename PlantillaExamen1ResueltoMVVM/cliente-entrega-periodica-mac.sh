#!/bin/bash
set -u

# ============================================================
# ENTREGA PERIODICA - EXAMEN (Android - macOS)
# Crea y envia un ZIP nuevo cada cierto tiempo.
# Excluye lo que un proyecto Android ignora en git (build/,
# .gradle/, .idea/, local.properties, *.iml, *.apk, *.aab, etc.)
# y tambien los ZIPs/scripts sueltos.
# ============================================================

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
SERVER_IP="192.168.70.166"
SERVER_PORT="3000"
INTERVAL_MINUTES=10

echo ""
echo "=================================================="
echo "      ENTREGA PERIODICA DE EXAMEN (Android)"
echo "=================================================="
echo ""

read -r -p "Nombre completo: " STUDENT_NAME
read -r -p "Codigo de estudiante: " STUDENT_CODE

if [ -z "$STUDENT_NAME" ]; then
    echo ""
    echo "Error: debes ingresar tu nombre."
    read -r -p "Presiona Enter para salir..." _
    exit 1
fi

if [ -z "$STUDENT_CODE" ]; then
    echo ""
    echo "Error: debes ingresar tu codigo."
    read -r -p "Presiona Enter para salir..." _
    exit 1
fi

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

WAIT_SECONDS=$((INTERVAL_MINUTES * 60))
SAFE_NAME="$(printf '%s' "$STUDENT_NAME" | tr ' ' '_')"

json_escape() {
    printf '%s' "$1" | sed 's/\\/\\\\/g; s/"/\\"/g'
}

NAME_ESC="$(json_escape "$STUDENT_NAME")"
CODE_ESC="$(json_escape "$STUDENT_CODE")"

while true; do
    echo ""
    echo "--------------------------------------------------"
    echo "Preparando nueva entrega..."
    echo "--------------------------------------------------"

    NOW="$(date '+%Y-%m-%d_%H-%M-%S')"
    STAGE_DIR="$(mktemp -d -t examen_stage)"
    TEMP_ZIP="$(mktemp -t examen).zip"
    rm -f "$TEMP_ZIP" 2>/dev/null
    FILENAME="${STUDENT_CODE}_${SAFE_NAME}_${NOW}.zip"

    echo "Nombre:   $STUDENT_NAME"
    echo "Codigo:   $STUDENT_CODE"
    echo "Fecha:    $NOW"
    echo ""

    echo "Preparando archivos del proyecto..."

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
        --exclude "*.zip" \
        --exclude "*.sh" \
        --exclude "*.bat" \
        "$PROJECT_DIR"/ "$STAGE_DIR"/

    if [ $? -ne 0 ]; then
        echo ""
        echo "ERROR: No se pudo preparar los archivos del proyecto."
        rm -rf "$STAGE_DIR" 2>/dev/null
    else
        echo "Creando ZIP nuevo..."

        (cd "$STAGE_DIR" && zip -rq "$TEMP_ZIP" .)

        if [ $? -ne 0 ] || [ ! -f "$TEMP_ZIP" ]; then
            echo ""
            echo "ERROR: No se pudo crear el ZIP."
        else
            echo "ZIP creado. Enviando al servidor..."

            ZIP_B64="$(base64 < "$TEMP_ZIP" | tr -d '\n')"

            BODY="$(printf '{"studentName":"%s","studentCode":"%s","filename":"%s","zipBase64":"%s","timestamp":"%s"}' \
                "$NAME_ESC" "$CODE_ESC" "$FILENAME" "$ZIP_B64" "$NOW")"

            RESPONSE="$(curl -s -m 30 -X POST "http://$SERVER_IP:$SERVER_PORT/submit" \
                -H "Content-Type: application/json" \
                -d "$BODY")"

            if printf '%s' "$RESPONSE" | grep -q '"ok":true'; then
                echo "Entrega recibida correctamente."
            else
                echo "ERROR: No se pudo enviar la entrega."
                echo "$RESPONSE"
            fi
        fi

        rm -f "$TEMP_ZIP" 2>/dev/null
        rm -rf "$STAGE_DIR" 2>/dev/null
    fi

    echo ""
    echo "Proxima entrega en $INTERVAL_MINUTES minutos..."
    sleep "$WAIT_SECONDS"
done
