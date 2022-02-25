#!/bin/bash
export POS_BACKEND_HOST=192.168.64.1
docker-compose -f docker-compose-grafana.yml up -d
