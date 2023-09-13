FROM openjdk:19
EXPOSE 8080
ADD "backend/target/app.jar" app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]