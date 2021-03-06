#!/bin/bash
action=$1
case $action in 
  grafana)
    export POS_BACKEND_HOST=192.168.1.122
    export POS_DB_HOST_MASTER=192.168.0.11
    export POS_DB_HOST_SLAVE1=192.168.0.12
    export POS_DB_HOST_SLAVE2=192.168.0.13
    export POS_DB_HOST_SLAVE3=192.168.0.14
    docker-compose -f docker-compose-grafana.yml up -d
    ;;
  monitoring)
    docker-compose -f docker-compose-monitoring.yml up -d
    ;;
  *)
    echo 'Invalid action'
    ;;
esac
