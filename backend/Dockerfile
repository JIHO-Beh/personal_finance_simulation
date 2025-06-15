# OpenJDK 17을 기반 이미지로 사용 (Java 17 버전)
FROM openjdk:17-jdk-slim

# 컨테이너 안에서 작업할 기본 디렉토리 설정
WORKDIR /app

# Gradle Wrapper 및 필요한 빌드 설정 파일들을 먼저 복사 (캐시 활용을 위해 중요)
COPY gradlew .
RUN chmod +x gradlew
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# (선택 사항) 멀티 모듈 프로젝트인 경우, 서브 모듈의 build.gradle도 복사
# COPY my-module/build.gradle my-module/

# 프로젝트 의존성을 다운로드하고 빌드 (코드가 변경되어도 이 단계는 재사용될 수 있음)
RUN ./gradlew dependencies

# 모든 소스 코드 파일을 컨테이너의 작업 디렉토리로 복사
COPY src src/

# Spring Boot 애플리케이션 빌드 (.jar 파일 생성)
# 테스트는 제외하여 빌드 시간을 단축합니다.
RUN ./gradlew bootJar -x test

# 빌드된 JAR 파일을 컨테이너로 복사
# 'build/libs/' 폴더에 생성되는 실제 JAR 파일 이름 패턴을 확인하고 필요하면 수정해주세요.
# 대부분의 경우 '프로젝트명-버전.jar' 형태입니다.
# 예시: COPY build/libs/backend-0.0.1-SNAPSHOT.jar app.jar
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 컨테이너가 시작될 때 실행될 명령어 (Spring Boot 애플리케이션 실행)
ENTRYPOINT ["java", "-jar", "app.jar"]

# Spring Boot 애플리케이션이 사용하는 기본 포트 8080을 외부에 노출
EXPOSE 8080