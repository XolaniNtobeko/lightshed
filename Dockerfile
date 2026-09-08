# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy Maven configuration first for layer caching
COPY pom.xml .

RUN mvn dependency:go-offline

# Copy source code and dataset
# Copy source code and dataset
COPY src ./src
COPY src/main/resources/town.csv .

# Package executable fat JAR
RUN mvn package -DskipTests


# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the fat JAR and CSV from builder stage
COPY --from=builder /app/target/lightshed-placename-service-1.0-SNAPSHOT.jar app.jar
COPY --from=builder /app/src/main/resources/town.csv .

# LightShed runs on port 7000
EXPOSE 7000

# Start application
ENTRYPOINT ["java", "-jar", "app.jar"]