FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies (to leverage Docker cache)
COPY pom.xml .
COPY src ./src

# Build Spring Boot application JAR
RUN mvn clean package -DskipTests

# ===========================
# Stage 2: Run with OpenJDK 21
# ===========================
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy only the built JAR from the build stage
COPY --from=build /app/target/*.jar secure-software.jar
COPY ./postgresql/server.crt /app/certs/root.crt

RUN chmod 444 /app/certs/root.crt

EXPOSE 8443

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "secure-software.jar"]
