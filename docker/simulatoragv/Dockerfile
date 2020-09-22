FROM openjdk:8-jre
COPY application.yml application.yml
COPY simulatoragv.jar simulatoragv.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","simulatoragv.jar"]