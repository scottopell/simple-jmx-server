# Use the official Maven image as the base image
FROM maven:latest AS build

# Set the working directory to /app
WORKDIR /app

# Copy the pom.xml file and install the dependencies
COPY pom.xml .
RUN mvn dependency:resolve

# Copy the source code and build the JAR file
COPY src/ /app/src/
RUN mvn clean package assembly:single

RUN ls /app/target

# Use the official OpenJDK image as the base image for the final image
FROM openjdk:latest AS final

# Set the working directory to /app
WORKDIR /app

# Copy the JAR file from the Maven image to the final image
COPY  --from=build /app/target/jmx-server-1.0.0-jar-with-dependencies.jar .

# Run the jar
CMD ["java", "-jar", "jmx-server-1.0.0-jar-with-dependencies.jar", "localhost", "1099"]
