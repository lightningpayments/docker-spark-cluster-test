#!/bin/bash

set -e

docker build --no-cache --tag docker-spark-test-app:3.0.0 -f docker-spark-test-app:3.0.0.Dockerfile .
docker build --no-cache --tag spark-base:3.1.2 -f spark-base:3.1.2.Dockerfile .
docker build --no-cache --tag spark-master:3.1.2 -f spark-master:3.1.2.Dockerfile .
docker build --no-cache --tag spark-worker:3.1.2 -f spark-worker:3.1.2.Dockerfile .

