FROM openjdk:17-jdk-slim

ARG JAR_FILE=build/libs/*.jar
WORKDIR /app

COPY ${JAR_FILE} server.jar
COPY sample sample

RUN mkdir -p /app/config

CMD ["java", "-jar", "/app/server.jar", "--spring.config.additional-location=/app/config/"]