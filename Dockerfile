FROM gradle-9.8.2-jdk-21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar --no-daemon

FROM   eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /home/grade/src/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT["java", "-jar", "app.jar"]
