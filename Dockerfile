FROM openjdk:11

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

COPY target/spring-boot-news-portal-0.0.1.jar /usr/src/app

ENTRYPOINT ["java","-jar","spring-boot-news-portal-0.0.1.jar"]