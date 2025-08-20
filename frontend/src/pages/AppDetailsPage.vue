<template>
  <b-loading v-if="isLoading" :is-full-page="false" :active="isLoading" />
  <div>
    {{ isLoading }}
    {{ task }}
    App Details Page
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

import { useTaskStore } from '@/stores/taskStore';
import type { App } from '@/types/types';

const route = useRoute();
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