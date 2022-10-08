FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} springwebflux-4connect.jar
ENTRYPOINT ["java","-jar","/springwebflux-4connect.jar"]
