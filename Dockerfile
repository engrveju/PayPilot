FROM openjdk:8
EXPOSE 8080
ADD target/pay-pilot.jar pay-pilot.jar
ENTRYPOINT ["java","-jar","/pay-pilot.jar"]