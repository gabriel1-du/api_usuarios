# Etapa 1: Construcción
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Tope de memoria para MAVEN dentro del build (t3.micro = 1 GB).
# -Xmx512m fija el límite real del proceso de compilación.
# MaxRAMPercentage queda como referencia: -Xmx tiene prioridad sobre él.
ENV MAVEN_OPTS="-Xms128m -Xmx512m -XX:MaxRAMPercentage=75"

COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución (JRE 21, sin Alpine para evitar problemas con JDBC/RDS)
FROM eclipse-temurin:21-jre
WORKDIR /app

# La JVM usa el 75% de los 768 MiB que Docker le asigna (~576 MiB de heap),
# arrancando con 64 MiB. ENTRYPOINT se mantiene igual.
ENV JAVA_TOOL_OPTIONS="-Xms64m -XX:MaxRAMPercentage=75.0"

# Copia el JAR generado desde la etapa de construcción
COPY --from=build /app/target/api_usuarios-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]