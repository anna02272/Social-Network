FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/socialNetwork-0.0.1-SNAPSHOT.jar /app/socialNetwork.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "socialNetwork.jar"]
