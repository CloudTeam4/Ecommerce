## Dockerfile
## jdk17 Image Start
#FROM openjdk:17-alpine
#
## 인자 설정 - JAR_File
#ARG JAR_FILE=build/libs/delivery_system-0.0.1-SNAPSHOT.jar
#
## jar 파일 복제
#COPY ${JAR_FILE} app.jar
#
#EXPOSE 8080
#
## 실행 명령어
#ENTRYPOINT ["java", "-jar", "app.jar"]