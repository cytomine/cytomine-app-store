import { createApp } from 'vue';
import { createPinia } from 'pinia';

import Buefy from 'buefy';
import 'buefy/dist/buefy.css';
import 'bulma/css/bulma.css';
import '@fortawesome/fontawesome-free/css/all.css';

import App from './App.vue';
import router from './router';

const app = createApp(App);

app.use(createPinia());
app.use(Buefy, { defaultIconPack: 'fas' });
app.use(router);

app.mount('#app');
