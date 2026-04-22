#!/bin/sh

export PORT="${PORT:-80}"
export API_BASE_URL="${API_BASE_URL:-http://localhost:8083}"

envsubst '${API_BASE_URL} ${PORT}' < /app/nginx.conf > /etc/nginx/conf.d/default.conf

exec nginx -g 'daemon off;'
