FROM openjdk:17-jdk-alpine

WORKDIR /app

EXPOSE 8087

    
ADD ./target/health-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]