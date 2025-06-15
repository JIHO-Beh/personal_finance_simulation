const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    // Docker Compose의 frontend 서비스 컨테이너 내부 포트와 일치해야 합니다.
    // docker-compose.yml에서 frontend의 ports 설정이 "8082:3000"이라면, 여기는 "3000"으로 설정합니다.
    // 만약 Vue CLI가 8080을 사용한다면, 여기도 8080으로 설정해야 합니다.
    // vue create 시 기본 포트 설정이 8080인 경우가 많습니다.
    port: 8080, // TODO: Vue 개발 서버의 실제 기본 포트 (보통 8080, 3000, 5173 등)로 설정!
    host: '0.0.0.0', // Docker 컨테이너 외부(호스트)에서 접근 가능하게 함

    // 백엔드 API 요청을 위한 프록시 설정
    proxy: {
      '/api': { // '/api'로 시작하는 모든 요청을 프록시합니다. (예: /api/users)
        target: process.env.VUE_APP_API_BASE_URL || 'http://localhost:8080', // 백엔드 URL
        // docker-compose.yml의 frontend 환경 변수 VUE_APP_API_BASE_URL을 사용합니다.
        // 이 변수는 'http://backend:8080'으로 설정되어 있습니다.
        changeOrigin: true, // 대상 서버의 출처를 변경하여 CORS 문제를 해결합니다.
        ws: true, // 웹소켓 프록시 활성화
        logLevel: 'debug' // 프록시 요청에 대한 로그를 출력 (디버깅용)
      }
    }
  }
})
