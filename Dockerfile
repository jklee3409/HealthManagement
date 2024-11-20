FROM openjdk:21-jdk

WORKDIR /app

COPY target/personalized-health-service-0.0.1-SNAPSHOT.jar app.jar

ENV DB_HOST=mysql-container

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
