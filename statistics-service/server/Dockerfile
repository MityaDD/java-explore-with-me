FROM amazoncorretto:11.0.19-alpine
COPY target/*.jar statistics-service.jar
ENTRYPOINT ["java","-jar","/statistics-service.jar"]