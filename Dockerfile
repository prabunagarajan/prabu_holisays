# Use the official OpenJDK 8 image as the base image
FROM openjdk:8-jdk-alpine

# Install tzdata for timezone configuration
RUN apk --no-cache add tzdata

# Set the default timezone
ENV TZ=Asia/Kolkata

# Set the working directory inside the container
WORKDIR /usr/src/app

# Copy the JAR file created by Maven from target folder to the container
COPY target/devarCabs.jar /usr/src/app/app.jar

# Expose the port that the application will run on (Spring Boot default is 8080)
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "/usr/src/app/app.jar"]
