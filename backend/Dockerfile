# --- 1단계: 소스 코드를 빌드하여 JAR 파일을 생성 ---
FROM gradle:8.8-jdk17-alpine AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
# 이 Dockerfile 안에서 직접 gradlew build를 실행합니다.
RUN gradle build --no-daemon -x test

# --- 2단계: 완성된 JAR 파일만 넣어 실행 ---
FROM openjdk:17-slim
# 위 1 단계에서 생성된 JAR 파일을 복사해옵니다.
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
# Docker Compose 파일에서 환경변수를 통해 프로필을 주입할 것이므로, 여기서는 기본 실행 명령어만 둡니다.
ENTRYPOINT ["java", "-jar", "/app.jar"]