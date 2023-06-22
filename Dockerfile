FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/spring-boot-starter-parent-2.7.12.jar spring-boot-starter-parent.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "spring-boot-starter-parent.jar"]
