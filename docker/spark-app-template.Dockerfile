FROM openjdk:8-jre-alpine

ARG SPARK_APP_JAR

ADD $SPARK_APP_JAR /app.jar

ENTRYPOINT ["java","-Xmx4G","-jar","/app.jar"]
