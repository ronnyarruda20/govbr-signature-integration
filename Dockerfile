# Etapa 1: build com Maven e JDK 17
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B -DskipTests package

# Etapa 2: imagem final só com o JRE
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/govbr-signature-integration-0.0.1-SNAPSHOT.jar app.jar
COPY assets ./assets
ENV SERVER_PORT=8080
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
