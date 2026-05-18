FROM maven:3.9.15 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
EXPOSE 8080 
WORKDIR /app
COPY --from=build /app/target/auth-service-*.jar app.jar
ENTRYPOINT [ "java", "-jar", "app.jar"]