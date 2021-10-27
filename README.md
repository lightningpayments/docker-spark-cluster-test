# SparkTestApp

Docker Apache Spark configuration app example.

```
# all dockerfiles in folder 'docker'

# start example
cd docker
bash build-images.sh
docker-compose -f spark-testapp-cluster-docker-compose.yml up -d
```