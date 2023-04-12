FROM openjdk17-alpine
EXPOSE 8080
ADD target/pay-pilot.jar pay-pilot.jar
ENTRYPOINT ["java","-jar","/pay-pilot.jar"]