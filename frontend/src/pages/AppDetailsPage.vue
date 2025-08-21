<template>
  <div class="content-wrapper">
    <b-loading v-if="isLoading" :is-full-page="false" :active="isLoading" />

    <div v-if="task" class="panel pb-5">
      <p class="panel-heading">
        <b-button class="is-link" icon-pack="fa" icon-left="angle-left" @click="router.back()">
          {{ t('go-back') }}
        </b-button>
      </p>

      <div class="panel-block">
        <section class="media">
          <figure class="media-left fixed-image">
            <app-image :namespace="task.namespace" :version="task.version" />
          </figure>
          <div class="media-content m-1 pt-2">
            <div class="content">
              <p>
                <strong class="is-size-2">{{ task.name }}</strong>
                <br>
                <span>
                  <app-author
                    v-for="(author, index) in task.authors"
                    :key="index"
                    :author="author"
                    class="mb-2 block"
                  />
                </span>
              </p>
            </div>

            <div class="media-content">
              <div class="card">
                <div class="card-header">
                  <p class="card-header-title">
                    {{ t("description") }}
                  </p>
                </div>

                <div class="card-content">
                  <div class="content">
                    {{ task.description || t('no-description') }}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>
      </div>

      <section class="level mt-5">
        <div class="level-item has-text-centered">
          <div>
            <p class="heading">Date</p>
            <p class="title">{{ task.date || t('unknown') }}</p>
          </div>
        </div>

        <div class="level-item has-text-centered">
          <div>
            <p class="heading">Version</p>
            <p class="title">{{ task.version }}</p>
          </div>
        </div>
      </section>
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
