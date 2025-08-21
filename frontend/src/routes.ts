import { createRouter, createWebHistory } from 'vue-router';

import AppDetailsPage from '@/pages/AppDetailsPage.vue';
import AppStore from '@/pages/AppStore.vue';
import MyAppsPage from '@/pages/MyAppsPage.vue';

const routes = [
  {
    path: '/',
    component: AppStore,
  },
  {
    path: '/my-apps',
    component: MyAppsPage,
  },
  {
    name: 'AppDetails',
    path: '/app/:namespace/:version',
    component: AppDetailsPage,
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: routes,
});

export default router;
