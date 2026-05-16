FROM maven:3.9.15 AS build
WORKDIR /app
COPY pom.xml .
COPY src .
RUN mvn clean package

FROM eclipse-temurin:21-jre
EXPOSE 8081 
WORKDIR /app
COPY --from=build /app/target/auth-service-*.jar .
ENTRYPOINT [ "java", "-jar", "auth-service-*.jar"]