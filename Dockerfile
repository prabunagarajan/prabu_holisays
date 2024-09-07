# Stage 1: Build the application using Maven
FROM maven:3.6.3-openjdk-8 AS build

# Set the working directory inside the container
WORKDIR /usr/src/app

# Copy the pom.xml and download dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copy the entire project source and build the JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application using the built JAR
FROM openjdk:8-jdk-alpine

# Install tzdata for timezone configuration
RUN apk --no-cache add tzdata

# Set the default timezone
ENV TZ=Asia/Kolkata

# Set the working directory for the runtime environment
WORKDIR /usr/src/app

# Copy the JAR from the build stage
COPY --from=build /usr/src/app/target/devarCabs.jar /usr/src/app/app.jar

# Expose the port the application will run on
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "/usr/src/app/app.jar"]
