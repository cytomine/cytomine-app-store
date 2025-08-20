<template>
  <b-loading v-if="isLoading" :is-full-page="false" :active="isLoading" />
  <div>
    {{ isLoading }}
    App Details Page
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';

import { getTask } from '@/api/tasks';
import type { App } from '@/types/types';

const { app } = defineProps<{
  app: App;
}>();

const task = ref<App | null>(null);
const isLoading = ref(true);
const error = ref<string | null>(null);

onMounted(async () => {
  try {
    task.value = await getTask(app.namespace, app.version);
  } catch (err) {
    error.value = 'Failed to load task';
    console.error(err);
  } finally {
    isLoading.value = false;
  }
});
</script>