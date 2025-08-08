import { createApp } from 'vue'
import App from './App.vue'
import router from './router'; // ルーターインスタンスインポート
import { createPinia } from 'pinia'

// Vuetify
import { createVuetify } from 'vuetify';
import * as components from 'vuetify/components'; // すべての Vuetify コンポネント
import * as directives from 'vuetify/directives';
import 'vuetify/styles/main.css';
import '@mdi/font/css/materialdesignicons.css';

const vuetify = createVuetify({
  components,
  directives,
  icons: { // Material Design Icons 使用設定
    defaultSet: 'mdi',
  },
  theme: {
    defaultTheme: 'customTheme',
    themes: {
      customTheme: {
        dark: false,
        colors: {
          primary: '#5E35B1',
          secondary: '#B39DDB',
          error: '#B00020',
          info: '#2196F3',
          success: '#4CAF50',
          warning: '#FB8C00',
        },
      },
    },
  },
  defaults: {
    VBtn: {
      variant: 'plain',
    },
    VList: {
      color: 'primary',
    },
  },
});

// 1. Pinia 인스턴스 생성
const pinia = createPinia()

const app = createApp(App);

app.use(router); // Vueにプラグインを適用
app.use(vuetify); // Vuetify プラグイン追加
app.use(pinia)

app.mount('#app');
