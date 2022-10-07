FROM openjdk:18
EXPOSE 8081
COPY /target/*.jar /usr/local/lib/backend.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/backend.jar"]