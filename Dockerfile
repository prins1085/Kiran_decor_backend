# Stage 1: Build the app
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test --no-daemon

# Stage 2: Run the app
FROM openjdk:17-jdk-slim
WORKDIR /app

# Install font libraries and configs
RUN apt-get update && \
    apt-get install -y libfreetype6 fonts-dejavu-core fontconfig && \
    rm -rf /var/lib/apt/lists/*

COPY --from=build /app/build/libs/QUOTEPRO-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9191
ENTRYPOINT ["java", "-jar", "app.jar"]

ENV JAVA_TOOL_OPTIONS="-Djava.awt.headless=true"