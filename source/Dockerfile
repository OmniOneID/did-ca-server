FROM openjdk:21-jdk-slim

WORKDIR /app

COPY did-ca-server/gradlew .
COPY did-ca-server/gradle gradle
COPY did-ca-server/build.gradle .
COPY did-ca-server/settings.gradle .
COPY did-ca-server/libs libs
COPY did-ca-admin /did-ca-admin
COPY did-ca-server/src src

RUN chmod +x ./gradlew

ENV GRADLE_OPTS="-Xmx2g -XX:MaxMetaspaceSize=512m"
RUN ./gradlew bootJar --no-daemon --max-workers=1

CMD ["java", "-jar", "/app/build/libs/did-ca-server-2.0.0.jar"]