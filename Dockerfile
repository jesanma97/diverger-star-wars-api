# Use a base image with Java 17
FROM openjdk:17-jdk-slim

# Maintainer information
LABEL maintainer="jesanma97@gmail.com"

# Path in the image where the application will be stored
WORKDIR /app

# Copy the JAR file of the application to the container
COPY target/RestAPIStarWars-0.0.1-SNAPSHOT.jar diverger-star-wars-api.jar

# Exposes the port on which the application will be available
EXPOSE 8080

ENV SPRING_DATASOURCE_H2_CONSOLE_SETTINGS_WEB_ALLOW_OTHERS=true

# Command to run the application
ENTRYPOINT ["java","-jar","diverger-star-wars-api.jar"]