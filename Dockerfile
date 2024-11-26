# First stage: Use a Maven image with Java 21 to build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY Banking-App/pom.xml ./
RUN mvn dependency:go-offline -B

# Copy the source code
COPY Banking-App/src ./src

# Build the project
RUN mvn clean package -DskipTests

# Second stage: Use a smaller JDK image to run the application
FROM openjdk:21-jdk-slim AS runtime

# Set working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]