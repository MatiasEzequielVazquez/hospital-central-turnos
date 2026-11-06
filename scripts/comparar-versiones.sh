#!/usr/bin/env bash
# Evidencia antes/despues: ejecuta la version inicial y la refactorizada con los
# mismos escenarios de entrada y compara sus salidas de consola linea por linea.
#
# Uso (desde la raiz del repo, en Git Bash / Linux / macOS):
#   ./scripts/comparar-versiones.sh                 # estado-inicial vs HEAD
#   ./scripts/comparar-versiones.sh estado-inicial estado-refactorizado
set -euo pipefail

ANTES="${1:-estado-inicial}"
DESPUES="${2:-HEAD}"
RAIZ="$(git rev-parse --show-toplevel)"
TMP="$(mktemp -d)"

limpiar() {
    git -C "$RAIZ" worktree remove --force "$TMP/antes" >/dev/null 2>&1 || true
    git -C "$RAIZ" worktree remove --force "$TMP/despues" >/dev/null 2>&1 || true
    rm -rf "$TMP"
}
trap limpiar EXIT

compilar() {
    local nombre="$1" ref="$2"
    git -C "$RAIZ" worktree add --detach "$TMP/$nombre" "$ref" >/dev/null 2>&1
    # En Git Bash (Windows) javac es un programa nativo: las rutas dentro del archivo @ no se
    # convierten solas, asi que se pasan a formato Windows con cygpath si esta disponible.
    find "$TMP/$nombre/src/main/java" -name "*.java" \
        | { if command -v cygpath >/dev/null 2>&1; then cygpath -m -f -; else cat; fi; } \
        | sed 's/.*/"&"/' > "$TMP/$nombre-fuentes.txt"
    javac -d "$TMP/$nombre-bin" "@$TMP/$nombre-fuentes.txt"
}

ejecutar() {
    local nombre="$1" entrada="$2"
    java -Duser.language=en -Duser.country=US -cp "$TMP/$nombre-bin" hospital.Main < "$entrada" | tr -d '\r'
}

echo "Comparando $ANTES  vs  $DESPUES"
compilar antes "$ANTES"
compilar despues "$DESPUES"

diferencias=0
for entrada in "$RAIZ"/src/test/resources/escenarios/*.in; do
    escenario="$(basename "$entrada" .in)"
    ejecutar antes "$entrada" > "$TMP/$escenario.antes"
    ejecutar despues "$entrada" > "$TMP/$escenario.despues"
    lineas="$(wc -l < "$TMP/$escenario.antes" | tr -d ' ')"
    if diff -q "$TMP/$escenario.antes" "$TMP/$escenario.despues" >/dev/null; then
        echo "  IGUAL     $escenario ($lineas lineas de salida)"
    else
        echo "  DISTINTO  $escenario"
        diff "$TMP/$escenario.antes" "$TMP/$escenario.despues" || true
        diferencias=1
    fi
done

if [ "$diferencias" -eq 0 ]; then
    echo "Resultado: el comportamiento externo es identico en todos los escenarios."
else
    echo "Resultado: hay diferencias de comportamiento."
    exit 1
fi
