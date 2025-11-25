# Multi-stage build
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build the WAR file
RUN mvn clean package -DskipTests

# Runtime stage
FROM tomcat:10.1-jdk17

# Remove default webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the WAR file from build stage
COPY --from=build /app/target/backend-posto-1.0-SNAPSHOT.war /tmp/app.war

# Unpack WAR into ROOT directory
RUN mkdir -p /usr/local/tomcat/webapps/ROOT && \
    cd /usr/local/tomcat/webapps/ROOT && \
    jar -xvf /tmp/app.war && \
    rm /tmp/app.war

# Set environment variables defaults (can be overridden)
ENV DB_HOST=mysql
ENV DB_PORT=3306
ENV DB_NAME=gasosa
ENV DB_USER=addiz
ENV DB_PASS=senha

EXPOSE 8080

CMD ["catalina.sh", "run"]
