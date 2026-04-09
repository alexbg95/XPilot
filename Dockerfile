# ── Stage 1: Build ────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar pom.xml primero para cachear dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copiar código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests -q

# ── Stage 2: Runtime ───────────────────────────────────
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Crear usuario no-root por seguridad
RUN addgroup -S xpilot && adduser -S xpilot -G xpilot

# Copiar JAR desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Cambiar al usuario no-root
USER xpilot

# Exponer puerto
EXPOSE 8080

# Ejecutar con opciones optimizadas para contenedor
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
