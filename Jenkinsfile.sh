#!/bin/bash

set -x

#
# Build binaries using docker
#
docker build -t callforpapers-${BUILD_NUMBER} -f Dockerfile.build .

#
# Prepare distribution folder
#
rm -rf dist
mkdir dist
container=$(docker create callforpapers-${BUILD_NUMBER})
docker cp $container:/work/target/call-for-paper-0.0.1-SNAPSHOT.jar dist/call-for-paper.jar
docker rm $container
docker rmi callforpapers-${BUILD_NUMBER}
cp src/main/docker/Dockerfile dist/Dockerfile

#
# Build production docker image
#
docker build -t cfpio/callforpapers dist

#
# Push to Dockerhub
#
docker tag cfpio/callforpapers cfpio/callforpapers:1.0.${BUILD_NUMBER}
docker push cfpio/callforpapers:1.0.${BUILD_NUMBER}

docker tag cfpio/callforpapers cfpio/callforpapers:latest
docker push cfpio/callforpapers:latest
