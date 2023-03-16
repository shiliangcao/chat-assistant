FROM openjdk:8-jdk

WORKDIR /shiliang/program/bootapp
ADD build/libs/assistant-api.jar app.jar

EXPOSE 10001

VOLUME ["/shiliang/logs/bootapp"]

CMD ["/bin/bash", "-c", "exec java -jar app.jar >> /shiliang/logs/bootapp/app.log 2>&1"]