FROM alpine:3.17

RUN apk --no-cache add openjdk8-jre

WORKDIR /shiliang/program/bootapp
ADD build/libs/assistant-api.jar app.jar

EXPOSE 10001

VOLUME ["/shiliang/logs/bootapp"]

CMD ["/bin/sh", "-c", "exec java -jar app.jar >> /shiliang/logs/bootapp/app.log 2>&1"]