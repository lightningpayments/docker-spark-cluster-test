# Docker Deployment mit Apache Spark Scala App

## Docker Apache Spark configuration app example.

```bash
# load spark-app-docker-build.sh
bash spark-app-docker-build.sh
docker-compose -f spark-app-cluster-docker-compose-template.yml up
```

```bash
# alternative:

# all dockerfiles in folder 'docker'
# start example for this example app
>$: cd <application root path>
>$: sbt clean assembly
>$: cp target/scala-2.12/DockerSparkTest-assembly-1.0.0.jar docker/DockerSparkTest-assembly-1.0.0.jar
>$: cd docker/
>$: bash build-images-template.sh DockerSparkTest-assembly-1.0.0.jar
>$: docker-compose -f spark-app-cluster-docker-compose-template.yml up
```

