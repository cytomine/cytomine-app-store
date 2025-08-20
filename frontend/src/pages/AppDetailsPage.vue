<template>
  <div class="content-wrapper">
    <b-loading v-if="isLoading" :is-full-page="false" :active="isLoading" />

    <div class="panel">
      <p class="panel-heading">
        <b-button class="is-link" icon-pack="fa" icon-left="angle-left" @click="router.back()">
          {{ t('go-back') }}
        </b-button>
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

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