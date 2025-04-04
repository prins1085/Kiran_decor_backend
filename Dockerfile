# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build output to the container
COPY build/libs/quotepro-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 9191

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]