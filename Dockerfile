# Etapa 1: Construcción
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución (JRE 21, sin Alpine para evitar problemas con JDBC/RDS)
FROM eclipse-temurin:21-jre
WORKDIR /app
# Copia el JAR generado desde la etapa de construcción
COPY --from=build /app/target/api_usuarios-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]