import { createApp } from 'vue';
import { createPinia } from 'pinia';

import Buefy from 'buefy';
import './assets/styles/main.css';

import App from './App.vue';
import i18n from './i18n';
import router from './routes';

const app = createApp(App);

app.use(createPinia());
app.use(Buefy, { defaultIconPack: 'fas' });
app.use(i18n);
app.use(router);

app.mount('#app');
