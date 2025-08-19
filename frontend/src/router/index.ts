import { createRouter, createWebHistory } from 'vue-router';

import AppStore from '@/pages/AppStore.vue';
import GlobalDashboard from '@/pages/GlobalDashboard.vue';
import MyAppsPage from '@/pages/MyAppsPage.vue';

const routes = [
  {
    path: '/',
    component: GlobalDashboard,
  },
  {
    path: '/my-apps',
    component: MyAppsPage,
  },
  {
    path: '/store',
    component: AppStore,
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: routes,
});

export default router;
