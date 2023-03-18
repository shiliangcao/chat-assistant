FROM alpine:3.17

RUN apk --no-cache add openjdk8-jre

WORKDIR /shiliang/program/bootapp

COPY setenv.sh setenv.sh
COPY src/main/resources/logback-prod.xml logback-prod.xml

ADD build/libs/assistant-api.jar app.jar

EXPOSE 10001

VOLUME ["/shiliang/logs/bootapp"]

CMD ["/bin/sh", "-c", "source setenv.sh && exec java $JVM_OPTIONS -jar app.jar >> /shiliang/logs/bootapp/app.log 2>&1"]