import { createRouter, createWebHistory } from 'vue-router';
import top from '../pages/Top.vue'; // 예시 컴포넌트
import FinancialSimulation from '../pages/FinancialSimulation.vue'; // 예시 컴포넌트
import DefaultLayout from '../layouts/DefaultLayout.vue';
// 1. 라우트 정의
// 각 라우트는 경로와 해당 경로에 매핑될 컴포넌트를 가집니다.
const routes = [
  {
    path: '/',
    component: DefaultLayout,
    meta: {
      auth: true,
    },
    children: [
      {
        path: '/',
        name: 'Top',
        component: top,
      },
      {
        path: '/financial-simulation',
        name: 'FinancialSimulation',
        component: FinancialSimulation, // HomeView 컴포넌트를 / 경로에 매핑
      },
    ],
  }
  
  // 추가 라우트들을 여기에 정의합니다.
  // {
  //   path: '/users/:id',
  //   name: 'user',
  //   component: () => import('../views/UserView.vue'),
  //   props: true // URL 파라미터(id)를 컴포넌트 props로 전달
  // },
];

// 2. 라우터 인스턴스 생성
const router = createRouter({
  history: createWebHistory(), // HTML5 History API를 사용하여 히스토리 모드 설정
  // history: createWebHashHistory(), // 해시 모드를 사용하려면 이 줄을 사용 (URL에 # 포함)
  routes, // 위에서 정의한 라우트들을 연결
});

// 3. 라우터 인스턴스 내보내기
export default router;