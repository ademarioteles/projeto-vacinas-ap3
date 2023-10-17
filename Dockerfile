FROM maven:3.8.3-openjdk-17 AS maven

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn package

FROM openjdk:17-alpine

WORKDIR /app

COPY --from=maven /app/target/ap3-0.0.1-SNAPSHOT.jar .

EXPOSE 8082

CMD ["java", "-jar", "ap3-0.0.1-SNAPSHOT.jar"]