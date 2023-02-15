# Definimos la imagen base
FROM openjdk:17

ARG JAR_FILE=target/*.jar

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos el archivo jar de nuestro proyecto a la imagen
COPY ${JAR_FILE} springwebflux-4connect.jar

# Exponemos el puerto 8080 (el puerto por defecto de Spring Webflux)
EXPOSE 8081

# Ejecutamos el archivo jar de nuestro proyecto
CMD ["java", "-jar", "/springwebflux-4connect.jar"]
