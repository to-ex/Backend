FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
ADD ${JAR_FILE} app.jar
CMD ["java", "-jar", "/app.jar"]