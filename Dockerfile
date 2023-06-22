FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} prom.jar
RUN mkdir -p /prom/temp

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","/prom.jar"]