FROM openjdk:17-jdk-slim
VOLUME /tmp
WORKDIR /app
ARG JAR_FILE=build/libs/farm-dora-auth-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} farm-dora-auth.jar
ENV JASYPT_KEY=${JASYPT_KEY}
EXPOSE 8020
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "farm-dora-auth.jar"]