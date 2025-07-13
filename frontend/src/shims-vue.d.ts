// src/shims-vue.d.ts

// CSS 파일을 TypeScript 모듈로 인식하게 합니다.
declare module '*.css' {
  const content: string;
  export default content;
}

// SCSS 파일을 TypeScript 모듈로 인식하게 합니다 (Vuetify는 Sass를 사용하므로 이 선언도 유용합니다).
declare module '*.scss' {
  const content: string;
  export default content;
}

// Vue 파일을 TypeScript 컴포넌트로 인식하게 합니다 (대부분의 Vue 프로젝트에 기본으로 포함되어 있음).
declare module '*.vue' {
  import type { DefineComponent } from 'vue';
  const component: DefineComponent<{}, {}, any>;
  export default component;
}