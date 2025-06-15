# Node.js 18 LTS 버전을 기반 이미지로 사용 (Vue 3 권장 버전)
FROM node:18-alpine

# 컨테이너 안에서 작업할 기본 디렉토리 설정
WORKDIR /app

# package.json 및 package-lock.json (또는 yarn.lock) 파일을 먼저 복사
# 의존성 변경 시에만 이 단계가 다시 실행되도록 캐싱 활용 (중요)
COPY package*.json ./

# 필요한 Node.js 의존성 설치
RUN npm install

# 모든 프론트엔드 소스 코드 파일을 컨테이너로 복사
COPY . .

# Vue 개발 서버가 사용하는 기본 포트 8080을 외부에 노출 (vue.config.js에서 설정된 포트와 일치해야 함)
EXPOSE 8080

# 컨테이너가 시작될 때 실행될 명령어 (Vue 개발 서버 실행)
# "--host 0.0.0.0"은 컨테이너 외부(내 컴퓨터 브라우저)에서 접근 가능하게 합니다.
# Vue CLI는 보통 'npm run serve'를 사용합니다.
CMD ["npm", "run", "serve", "--", "--host", "0.0.0.0"]