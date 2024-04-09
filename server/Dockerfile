FROM openjdk:17-jdk-alpine
WORKDIR /server
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN ./mvnw clean package -DskipTests
ENTRYPOINT ["java", "-jar", "target/server-3.2.4.jar"]