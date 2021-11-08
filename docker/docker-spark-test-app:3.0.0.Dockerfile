FROM openjdk:8-jre-alpine

ADD DockerSparkTest-assembly-3.0.0.jar /app.jar

ENTRYPOINT ["java","-Xmx4G","-jar","/app.jar"]
