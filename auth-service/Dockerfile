FROM eclipse-temurin:21-jre
EXPOSE 8080 
WORKDIR /app
COPY target/auth-service-*.jar app.jar
ENTRYPOINT [ "java", "-jar", "app.jar"]