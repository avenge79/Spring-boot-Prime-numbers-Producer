# Use the official OpenJDK base image
FROM openjdk:11-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the executable JAR file (make sure the JAR file is available in the same directory as the Dockerfile)
COPY target/producer-app-1.0.0.jar /app/producer.jar

# Expose the port the application listens on
EXPOSE 8081

# Command to run the Spring Boot application
CMD ["java", "-jar", "producer.jar"]