#!/bin/bash

set -e

if [ -n "$1" ]
then
  jar_file=$1

  docker build --no-cache --build-arg SPARK_APP_JAR=$jar_file --tag spark-app-template:latest -f spark-app-template.Dockerfile .
  docker build                                                --tag spark-base:3.1.2          -f spark-base:3.1.2.Dockerfile .
  docker build                                                --tag spark-master:3.1.2        -f spark-master:3.1.2.Dockerfile .
  docker build                                                --tag spark-worker:3.1.2        -f spark-worker:3.1.2.Dockerfile .

  docker-compose -f spark-app-cluster-docker-compose-template.yml up -d
else
  echo "Arg 1 must be spark app jar file, e.g. 'my-spark-app.jar'"
fi
