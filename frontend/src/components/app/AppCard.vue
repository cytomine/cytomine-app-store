<template>
  <div class="card">
    <div class="card-image">
      <figure class="image is-animated is-5by3">
        <img :src="app.imageUrl || 'https://bulma.io/assets/images/placeholders/1280x960.png'" alt="Placeholder image">
      </figure>
    </div>

    <div class="card-content">
      <div class="media">
        <div class="media-content">
          <p class="title is-4 less-bottom">{{ app.name }}</p>
          <time datetime="">{{ app.date }}</time>
        </div>
        <div class="media-right">
          <p class="subtitle is-6">{{ app.version }}</p>
        </div>
      </div>

      <div class="content">
        {{ app.description }}
      </div>

      <footer class="card-footer">
        <b-button class="card-footer-item" type="is-ghost" @click.prevent="handleDownload">
          {{ t('download') }}
        </b-button>
        <a href="#" class="card-footer-item">More</a>
      </footer>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n';

import { useTask } from '@/composables/useTask';
import type { App } from '@/types/types.ts';

const { app } = defineProps<{
  app: App,
}>();

const { t } = useI18n();
const { downloadTask } = useTask();

const handleDownload = async () => {
  await downloadTask(app.namespace, app.version);
};
</script>

<style scoped>
.less-bottom {
  margin-bottom: 0.9rem !important;
}

.card {
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
  border-radius: 10px;
  transition: .2s ease-out;
}

.rounded {
  border-radius: 10px;
}

/* On mouse-over, add a deeper shadow + Bounce */
.card:hover {
  box-shadow: 0 1px 100px 0 rgba(38, 63, 206, 0.3);
  transform: translate3d(0, -2px, 0);
}

/* Router-Link makes entire text blue so let's select everything except the links & make them black */
:not(a) {
  color: black;
}
</style>