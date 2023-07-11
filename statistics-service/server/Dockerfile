FROM amazoncorretto:11.0.19-alpine
COPY target/*.jar stat-service.jar
ENTRYPOINT ["java","-jar","/stat-service.jar"]