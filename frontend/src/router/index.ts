import { createRouter, createWebHistory } from 'vue-router';

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
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: routes,
});

export default router;
