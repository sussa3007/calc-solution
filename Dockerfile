# Base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the build file
COPY build/libs/*.jar /app/calc-0.0.1.jar

# Expose the port the application runs on
EXPOSE 8787

# Define entrypoint
ENTRYPOINT ["java", "-jar", "/app/calc-0.0.1.jar", "--spring.profiles.active=pro"]