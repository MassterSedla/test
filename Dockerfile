FROM openjdk:17-jdk-alpine

RUN apk --no-cache add curl && \
    curl -fsSL https://download.docker.com/linux/static/stable/x86_64/docker-20.10.7.tgz | tar zxvf - --strip 1 -C /usr/local/bin docker/docker

ARG JAR_FILE=target/test-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
COPY docker-compose.yaml /docker-compose.yaml
ENTRYPOINT ["java","-jar","/app.jar"]
