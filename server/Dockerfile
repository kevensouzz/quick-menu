FROM openjdk:17-jdk

WORKDIR /server

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw clean package -DskipTests

ENTRYPOINT ["java", "-jar", "target/server-0.0.1-SNAPSHOT.jar"]