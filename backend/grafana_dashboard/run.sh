#!/bin/bash
export POS_BACKEND_HOST=192.168.1.142
export POS_DB_HOST_MASTER=192.168.0.11
docker-compose -f docker-compose-grafana.yml up -d
