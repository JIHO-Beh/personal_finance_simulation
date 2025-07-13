import { createApp } from 'vue'
import App from './App.vue'
import router from './router'; // ルーターインスタンスインポート

// Vuetify
import 'vuetify/styles/main.css' // Vuetify 基本ス
import '@mdi/font/css/materialdesignicons.css'; // Material Design Icons CSS インポートタイルシート
import { createVuetify } from 'vuetify';
import * as components from 'vuetify/components'; // すべての Vuetify コンポネント
import * as directives from 'vuetify/directives';

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
          primary: '#23428E',
          secondary: '#66BB6A',
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

const app = createApp(App);

app.use(router); // Vueにプラグインを適用
app.use(vuetify); // Vuetify プラグイン追加

app.mount('#app');
