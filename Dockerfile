FROM openjdk:23-jdk
ARG JAR_FILE=target/*.jar
COPY ./target/defect-tracker-server-0.8.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]