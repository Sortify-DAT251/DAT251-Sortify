# 1: Build the JAR file
FROM gradle:7.6-jdk17-alpine AS builder

WORKDIR /app

# Copy the entire project
# COPY . .

# Copy Gradle-related files first to leverage caching
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./

# Copy the source code
COPY src src

# Ensure permissions for Gradle wrapper and build the JAR file
RUN chmod +x ./gradlew 
RUN ./gradlew clean bootJar --no-daemon

# Verify the contents of the /app/build/libs/ directory
RUN ls -l /app/build/libs/

# 2: Run the application
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the built JAR file
COPY --from=builder /app/build/libs/*.jar /app/app.jar

# Copy the gradlew script for running tests
COPY --from=builder /app/gradlew /app/gradlew
COPY --from=builder /app/gradle /app/gradle

# Ensure permissions for Gradle wrapper
RUN chmod +x /app/gradlew

# Verify the contents of the /app/ directory
RUN ls -l /app/

# Expose the application port
EXPOSE 9876

# Command to run the application
CMD ["java", "-jar", "/app/app.jar"]
