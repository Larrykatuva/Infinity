FROM openjdk:17-jdk

WORKDIR /app

COPY target/infinity-0.0.1-SNAPSHOT.jar /app/infinity.jar

EXPOSE 8080

CMD ["java", "-jar", "infinity.jar"]