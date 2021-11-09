#!/bin/bash

set -e

docker-compose -f spark-app-cluster-docker-compose-template.yml up --build