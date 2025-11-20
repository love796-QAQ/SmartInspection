# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy parent pom
COPY pom.xml .

# Copy module poms
COPY smart-inspection-common/pom.xml smart-inspection-common/
COPY smart-inspection-system/pom.xml smart-inspection-system/
COPY smart-inspection-business/pom.xml smart-inspection-business/
COPY smart-inspection-api/pom.xml smart-inspection-api/
COPY smart-inspection-start/pom.xml smart-inspection-start/

# Download dependencies (cache layer)
# RUN mvn dependency:go-offline -B

# Copy source code
COPY smart-inspection-common/src smart-inspection-common/src
COPY smart-inspection-system/src smart-inspection-system/src
COPY smart-inspection-business/src smart-inspection-business/src
COPY smart-inspection-api/src smart-inspection-api/src
COPY smart-inspection-start/src smart-inspection-start/src

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
