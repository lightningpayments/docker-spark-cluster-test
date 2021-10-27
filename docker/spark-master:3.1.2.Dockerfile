FROM spark-base:3.1.2

ENV SPARK_MASTER_PORT 7077
ENV SPARK_MASTER_WEBUI_PORT 8080
ENV SPARK_MASTER_LOG /spark/logs

COPY start-master.sh /start-master.sh

EXPOSE 8080 7077 6066

CMD ["/bin/bash", "/start-master.sh"]
