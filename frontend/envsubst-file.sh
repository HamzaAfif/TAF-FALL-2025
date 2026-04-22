#!/bin/sh

: "${PORT:=8080}"
: "${API_BASE_URL:=http://localhost:8083}"

envsubst '${API_BASE_URL} ${PORT}' < /app/nginx.conf > /etc/nginx/conf.d/default.conf

exec nginx -g 'daemon off;'
