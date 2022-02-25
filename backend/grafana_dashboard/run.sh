#!/bin/bash
export POS_BACKEND_HOST=$(ifconfig en0 inet | grep inet | awk '{ print $2 }')
docker-compose -f docker-compose-grafana.yml up
