#!/usr/bin/env bash
set -e
target=$1
case $target in
  arm)
    ./mvnw clean package \
      -Pnative \
      -DskipTests \
      -Dquarkus.native.additional-build-args='--initialize-at-build-time=java.awt.color.ColorSpace$BuiltInSpace' \
      -Dquarkus.container-image.build=true \
      -Dquarkus.native.native-image-xmx=6g
    ;;
  container)
    ./mvnw package \
          -DskipTests \
          -Dquarkus.container-image.build=true \
          -Dquarkus.container-image.group=\
          -Dquarkus.container-image.tag=latest
    ;;
  *)
    ./mvnw clean package \
      -DskipTests \
      -Dquarkus.native.additional-build-args='--initialize-at-build-time=java.awt.color.ColorSpace$BuiltInSpace' \
      -Dquarkus.container-image.build=true
      -Dquarkus.container-image.group=\
      -Dquarkus.container-image.tag=latest \
      -Dquarkus.native.container-build=true \
      -Dquarkus.native.builder-image=quay.io/quarkus/ubi-quarkus-native-image:22.0-java17 \
      -Dquarkus.native.native-image-xmx=14g \
      -Dquarkus.native.container-runtime-options='-m=16g'
      #docker-compose build
    ;;
esac