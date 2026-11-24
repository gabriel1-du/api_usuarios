#!/usr/bin/env bash
set -euo pipefail

# ============================================================
# deploy.sh - Despliegue de api_usuarios en EC2 con docker compose
#
# Uso:
#   ./deploy.sh [rama]              Despliega de verdad
#   ./deploy.sh [rama] --check      Solo valida entorno/.env/compose (no modifica nada)
#   ./deploy.sh [rama] --dry-run    Muestra lo que haría, sin git pull ni docker up
#   ./deploy.sh --help
# ============================================================


# Inicializacion de variables
RAMA="master"
MODO="normal"
COMPOSE_FILE="docker-compose.prod.yml"
SERVICIO="api-usuarios-prod"

usage() {
  sed -n '4,11p' "$0"
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --check|--check-only) MODO="check"; shift ;;
    --dry-run)            MODO="dry-run"; shift ;;
    -h|--help)            usage; exit 0 ;;
    -*)                   echo "[deploy] Opción desconocida: $1"; usage; exit 1 ;;
    *)                    RAMA="$1"; shift ;;
  esac
done

log() { printf '[deploy] %s\n' "$*"; }

# Ejuta el comando, o solo lo muestra en modo check/dry-run
run() {
  if [[ "$MODO" == "check" || "$MODO" == "dry-run" ]]; then
    printf '[simulado]'; printf ' %q' "$@"; printf '\n'
  else
    "$@"
  fi
}

log "Modo: ${MODO} | rama: ${RAMA} | compose: ${COMPOSE_FILE}"

# --- 1. HERRAMIENTAS ---
for CMD in git docker; do
  if ! command -v "${CMD}" >/dev/null 2>&1; then
    log "ERROR: no encontré '${CMD}' en el PATH"
    exit 1
  fi
done

if ! docker compose version >/dev/null 2>&1; then
  log "ERROR: 'docker compose' no está disponible (usa docker compose v2)"
  exit 1
fi

# --- 2. ARCHIVOS Y VARIABLES ---
if [[ ! -f "${COMPOSE_FILE}" ]]; then
  log "ERROR: falta ${COMPOSE_FILE} en $(pwd)"
  exit 1
fi

if [[ ! -f ".env" ]]; then
  log "ERROR: falta .env en $(pwd) (copia .env.example y complétalo)"
  exit 1
fi

for VAR in DB_HOST DB_PORT DB_NAME DB_USER DB_PASSWORD SERVER_PORT; do
  if ! grep -Eq "^${VAR}=.+" .env; then
    log "ERROR: .env no tiene un valor para ${VAR}"
    exit 1
  fi
done

if grep -Eq '^DB_HOST=(localhost|127\.0\.0\.1)' .env; then
  log "ADVERTENCIA: DB_HOST apunta a localhost; en EC2 debe ser el endpoint de RDS"
fi

# Validación real del compose (interpola .env y comprueba la sintaxis)
if ! docker compose -f "${COMPOSE_FILE}" config -q; then
  log "ERROR: ${COMPOSE_FILE} no es válido"
  exit 1
fi
log "Compose válido ✅"

# --- 3. GIT ---
if ! git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
  log "ERROR: este directorio no es un repositorio git"
  exit 1
fi

if ! git rev-parse --verify --quiet "refs/heads/${RAMA}" >/dev/null; then
  log "La rama local '${RAMA}' no existe; se creará desde origin/${RAMA}"
  run git fetch origin "${RAMA}"
  run git checkout -b "${RAMA}" "origin/${RAMA}"
else
  run git fetch origin
  run git checkout "${RAMA}"
fi
run git pull origin "${RAMA}"

# --- 4. CONSTRUCCIÓN Y ARRANQUE ---
run sudo docker compose -f "${COMPOSE_FILE}" up -d --build
run sudo docker image prune -f

# --- 5. COMPROBACIÓN (solo en modo real) ---
if [[ "$MODO" == "normal" ]]; then
  PUERTO=$(grep -E '^SERVER_PORT=' .env | head -1 | cut -d= -f2)
  PUERTO="${PUERTO:-8080}"

  if command -v curl >/dev/null 2>&1; then
    log "Esperando a que la API responda en puerto ${PUERTO}..."
    OK=0
    for _ in $(seq 1 30); do
      if curl -fsS "http://localhost:${PUERTO}/api/usuariosApi/" >/dev/null 2>&1; then
        OK=1
        break
      fi
      sleep 2
    done
    if [[ "$OK" -eq 1 ]]; then
      log "Healthcheck OK ✅"
    else
      log "ERROR: la API no respondió. Revisa: sudo docker compose -f ${COMPOSE_FILE} logs ${SERVICIO}"
      exit 1
    fi
  else
    log "ADVERTENCIA: curl no está instalado; se omite el healthcheck"
  fi

  log "Últimas líneas de Spring Boot:"
  sudo docker compose -f "${COMPOSE_FILE}" logs --tail=50 "${SERVICIO}"
  sudo docker compose -f "${COMPOSE_FILE}" ps
fi

log "Despliegue finalizado (modo: ${MODO})"
