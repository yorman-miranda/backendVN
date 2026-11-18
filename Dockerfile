FROM gradle:9.8.2-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN ls -la
RUN gradle bootJar --no-daemon
RUN ls -la build/libs || echo "LA CARPETA build/libs NO ESISTE"

FROM   eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]