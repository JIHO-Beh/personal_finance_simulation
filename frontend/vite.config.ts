import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vuetify from 'vite-plugin-vuetify';

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vuetify({ autoImport: true })
  ],
  resolve: { // @/ 경로 별칭이 없는 경우 추가
    alias: {
      '@': '/src',
    },
  },
  server: {
    // Docker 컨테이너 외부(NGINX)에서의 접속을 허용합니다. (403 에러 해결)
    host: true,
    
    // 컨테이너 내부에서 사용할 포트를 3000번으로 명시합니다.
    port: 3000, 
    strictPort: true,

    // NGINX가 사용하는 서비스 이름 'frontend'를 허용된 호스트로 추가합니다.
    allowedHosts: ['frontend'],

    // HMR(핫 리로딩)을 위해 브라우저가 NGINX 포트로 접속하도록 설정합니다. (WebSocket 에러 해결)
    hmr: {
      clientPort: 8082
    },

    // Docker 볼륨 마운트 시 파일 변경 감지를 위한 설정입니다. (핫 리로딩 안정성)
    watch: {
      usePolling: true 
    }
  }
})
