#!/bin/bash
export POS_BACKEND_HOST=$(ifconfig enp0s1 | grep inet | awk '{ print $2 }')
docker-compose -f docker-compose-grafana.yml up -d
