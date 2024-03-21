# jdk17 Image Start
FROM openjdk:17-jdk-slim

# 인자 설정 - JAR_File
ARG JAR_FILE=build/libs/ecommerce-0.0.1-SNAPSHOT.jar

# jar 파일 복제
COPY ${JAR_FILE} app.jar

# 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]