# Use a lightweight Java 17 base image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file (assuming you have a Spring Boot application)
COPY build/libs/*.jar app.jar

# Expose the application's port
EXPOSE 9876

# Run the application
CMD ["java", "-jar", "app.jar"]