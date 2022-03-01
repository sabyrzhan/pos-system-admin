#!/usr/bin/env bash
set -e
./mvnw clean package \
  -Pnative \
  -DskipTests \
  -Dquarkus.native.additional-build-args='--initialize-at-build-time=java.awt.color.ColorSpace$BuiltInSpace' \
  -Dquarkus.native.container-build=true \
  -Dquarkus.native.builder-image=quay.io/quarkus/ubi-quarkus-native-image:22.0-java17 \
  -Dquarkus.native.native-image-xmx=6g \
  -Dquarkus.native.container-runtime-options='-m=8g'

docker-compose build