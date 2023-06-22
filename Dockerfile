FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/Banking-App-0.0.1-SNAPSHOT.jar Banking-App-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "Banking-App-0.0.1-SNAPSHOT.jar"]
