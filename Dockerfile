# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy parent pom
COPY pom.xml .

# Copy module directories explicitly
COPY smart-inspection-common ./smart-inspection-common
COPY smart-inspection-system ./smart-inspection-system
COPY smart-inspection-business ./smart-inspection-business
COPY smart-inspection-api ./smart-inspection-api
COPY smart-inspection-start ./smart-inspection-start

# Debug: List files to verify structure (Optional, for verification)
RUN ls -R /app

# Build
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy jar from builder
COPY --from=builder /app/smart-inspection-start/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run
ENTRYPOINT ["java", "-jar", "app.jar"]
