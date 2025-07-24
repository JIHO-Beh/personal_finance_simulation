# 1. Java 17 버전을 실행할 수 있는 환경을 준비합니다.
FROM openjdk:17-slim

# 2. 나중에 빌드될 JAR 파일의 경로를 변수로 만듭니다.
ARG JAR_FILE=build/libs/*.jar

# 3. 위 경로의 JAR 파일을 컨테이너 안으로 복사하고, 이름을 app.jar로 바꿉니다.
COPY ${JAR_FILE} app.jar

# 4. 이 컨테이너가 시작될 때 "java -jar /app.jar" 명령어를 실행하라고 지시합니다.
# -Dspring.profiles.active=docker 옵션 추가
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "/app.jar"]