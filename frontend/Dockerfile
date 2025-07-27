# --- 1단계: 빌드 환경 (Node.js) ---
# Vue.js 소스 코드를 실제 웹사이트 파일(HTML, JS, CSS)로 만들기 위한 '공장'입니다.
FROM node:20-alpine AS build

WORKDIR /app
COPY package*.json ./
# yarn.lock 파일이 있으면 yarn을, 아니면 npm을 사용합니다.
RUN if [ -f yarn.lock ]; then yarn install --frozen-lockfile; else npm install; fi
COPY . .
# 'npm run build'를 실행하여 'dist' 폴더에 최종 결과물을 생성합니다.
RUN npm run build

# --- 2단계: 실행 환경 (NGINX) ---
# 완성된 웹사이트 파일을 사용자에게 보여주는 '서버'입니다.
FROM nginx:alpine

# 1단계 공장에서 만든 최종 결과물('dist' 폴더 안의 모든 파일)을
# NGINX 웹서버의 기본 폴더로 복사합니다.
COPY --from=build /app/dist /usr/share/nginx/html

# 컨테이너가 80번 포트를 사용한다고 알려줍니다.
EXPOSE 80

# NGINX 웹서버를 실행시킵니다.
CMD ["nginx", "-g", "daemon off;"]