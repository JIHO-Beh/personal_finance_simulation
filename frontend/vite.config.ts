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
    port: 3000, 
    host: '0.0.0.0',
    strictPort: true
  }
})
