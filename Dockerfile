FROM amazoncorretto:17
# FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} kkeujeok-backend-0.0.1-SNAPSHOT.jar
# COPY build/libs/*.jar my-project.jar
ENTRYPOINT ["java","-jar","/kkeujeok-backend-0.0.1-SNAPSHOT.jar"]

RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime