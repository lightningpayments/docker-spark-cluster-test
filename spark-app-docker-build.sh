#!/bin/bash

sbt clean assembly
cp target/scala-2.12/DockerSparkTest-assembly-3.0.0.jar docker/DockerSparkTest-assembly-3.0.0.jar
cd docker/
bash build-images-template.sh
