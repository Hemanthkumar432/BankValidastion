FROM maven:3.8.2-jdk-19 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:19-jdk-slim
COPY --from=build target/spring-boot-starter-parent-2.7.12.jar spring-boot-starter-parent.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "spring-boot-starter-parent.jar"]
