FROM openjdk:17 as base
WORKDIR /app
LABEL MAINTAINER ="Emmanuel Ugwueze"ugwuezeje@gmail.com""
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src ./src
COPY ./target/paypilot.jar  /opt/paypilot.jar
COPY . /opt/
ENTRYPOINT ["java", "-jar", "/opt/paypilot.jar", "--server.port=8080", "--spring.config.location=file:/opt/src/main/resources/"]

FROM base as development
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"]

FROM base as build
RUN ./mvnw package

FROM openjdk:17 as production
EXPOSE 8080
COPY --from=build /app/target/paypilot*.jar /paypilot.jar
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/paypilot.jar"]
FROM openjdk:17


