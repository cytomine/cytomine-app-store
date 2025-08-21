<template>
  <div class="card">
    <div class="card-image">
      <figure class="image is-animated is-5by3">
        <img
            :src="taskLogoUrl"
            @error="($event.target as HTMLImageElement).src='https://bulma.io/assets/images/placeholders/1280x960.png'"
            alt="Placeholder image">
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
        <RouterLink
          class="card-footer-item"
          :to="{ name: 'AppDetails', params: { namespace: app.namespace, version: app.version } }"
        >
          {{ t('show-more') }}
        </RouterLink>
      </footer>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n';

import type { App } from '@/types/types.ts';
import { computed } from 'vue';

const taskLogoUrl = computed(() => {
  const baseUrl = import.meta.env.VITE_API_BASE_URL;
  const { namespace, version } = props.app;

  if (!baseUrl || !namespace || !version) {
    return '';
  }

  return `${baseUrl}/api/v1/tasks/${namespace}/${version}/logo`;
});

const props = defineProps<{
  app: App,
}>();

const { t } = useI18n();
</script>

<style scoped>
:not(a) {
  color: black;
}

.card {
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
  border-radius: 10px;
  transition: .2s ease-out;
}

.card:hover {
  box-shadow: 0 1px 100px 0 rgba(38, 63, 206, 0.3);
  transform: translate3d(0, -2px, 0);
}

.less-bottom {
  margin-bottom: 0.9rem !important;
}

.rounded {
  border-radius: 10px;
}
</style>