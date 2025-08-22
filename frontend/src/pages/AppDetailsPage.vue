<template>
  <div class="content-wrapper">
    <b-loading v-if="isLoading" :is-full-page="false" :active="isLoading" />

    <div v-if="task" class="panel">
      <p class="panel-heading">
        <b-button class="is-link" icon-pack="fa" icon-left="angle-left" @click="router.back()">
          {{ t('go-back') }}
        </b-button>
      </p>

      <div class="card p-4">
        <section class="columns">
          <div class="column is-one-quarter">
            <figure class="image fixed-image">
              <app-image :namespace="task.namespace" :version="task.version" />
            </figure>
          </div>

          <div class="column">
            <div class="p-2">
              <h2 class="title is-4">{{ task.name }}</h2>
            </div>
            <div class="px-2 is-flex is-flex-wrap-wrap">
              <app-author v-for="(author, index) in task.authors" :key="index" :author="author" />
            </div>
          </div>
        </section>

        <section class="columns has-text-centered">
          <div class="column is-half">
            <div>
              <p class="heading">Date</p>
              <p class="title">{{ task.date || t('unknown') }}</p>
            </div>
          </div>

          <div class="column is-half">
            <div>
              <p class="heading">Version</p>
              <p class="title">{{ task.version }}</p>
            </div>
          </div>
        </section>

        <section class="card p-4">
          <h3 class="title is-5">{{ t("description") }}</h3>
          <p>
            {{ task.description || t('no-description') }}
          </p>
        </section>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import AppAuthor from '@/components/app/AppAuthor.vue';
import AppImage from '@/components/app/AppImage.vue';
import { useTaskStore } from '@/stores/taskStore';
import type { App } from '@/types/types';

const { t } = useI18n();
const route = useRoute();
const router = useRouter();
const taskStore = useTaskStore();

const task = ref<App | null>(null);
const isLoading = ref(true);

onMounted(async () => {
  try {
    task.value = await taskStore.fetchTask(
      route.params.namespace.toString(),
      route.params.version.toString(),
    );
  } finally {
    isLoading.value = false;
  }
});
</script>

<style scoped>
.logo {
  width: 17rem;
  height: 13rem;
  position: relative;
  overflow: hidden;
  border-radius: 15%;
}

.fixed-image {
  width: 25rem;
  height: auto;
}

.fixed-image img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  border-radius: 10px;
}
</style>
