import { createRouter, createWebHistory } from 'vue-router';

import GlobalDashboard from '@/pages/GlobalDashboard.vue';
import StoragePage from '@/pages/StoragePage.vue';

const routes = [
  {
    path: '/',
    component: GlobalDashboard,
  },
  {
    path: '/storage',
    component: StoragePage,
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: routes,
});

export default router;
