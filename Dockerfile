FROM maven:4.0.0-rc-4-ibm-semeru-25-noble AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/markenx-0.0.1-SNAPSHOT.jar app.jar

ARG JAVA_OPTS=""
ENV JAVA_OPTS=${JAVA_OPTS}

EXPOSE 8080

VOLUME /tmp

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
