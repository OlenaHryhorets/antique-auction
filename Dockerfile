FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN mkdir -p ~/antique-auction-images
RUN mkdir -p /home/antique-auction-images
COPY antique-auction-images /home/antique-auction-images
COPY antique-auction-images /root/antique-auction-images
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar"]
