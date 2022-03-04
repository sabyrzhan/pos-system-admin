#!/usr/bin/env bash
set -e
target=$1
case $target in
  arm)
    ./mvnw clean package \
      -Pnative \
      -DskipTests \
      -Dquarkus.native.additional-build-args='--initialize-at-build-time=java.awt.color.ColorSpace$BuiltInSpace' \
      -Dquarkus.container-image.tag=pos-system-backend \
      -Dquarkus.container-image.build=true \
      -Dquarkus.native.native-image-xmx=6g
    ;;
  *)
    ./mvnw clean package \
      -Pnative \
      -DskipTests \
      -Dquarkus.native.additional-build-args='--initialize-at-build-time=java.awt.color.ColorSpace$BuiltInSpace' \
      -Dquarkus.native.container-build=true \
      -Dquarkus.container-image.tag=latest \
      -Dquarkus.container-image.build=true \
      -Dquarkus.native.builder-image=quay.io/quarkus/ubi-quarkus-native-image:22.0-java17 \
      -Dquarkus.native.native-image-xmx=6g \
      -Dquarkus.native.container-runtime-options='-m=8g'
      #docker-compose build
    ;;
esac