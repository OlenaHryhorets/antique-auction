FROM mysql
ENV MYSQL_ROOT_PASSWORD="root"
ENV MYSQL_DATABASE="auction"
FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
