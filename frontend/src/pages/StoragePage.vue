<template>
  <div class="content-wrapper">
    <div class="panel">
      <p class="panel-heading">
        {{ $t('storage') }}
        <b-button>{{ $t('upload') }}</b-button>
      </p>

      <b-input class="search-input" v-model="searchString" icon="search" :placeholder="$t('search')" />

      <div class="panel-block">
        <AppCard v-for="task in tasks" :key="task.id" :app="task" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';

import AppCard from '@/components/app/AppCard.vue';
import { getAllTasks } from '@/api/tasks';
import type { App } from '@/types/types.ts';

const searchString = ref('');
const tasks = ref<App[]>([]);

onMounted(async () => {
  tasks.value = await getAllTasks();
});
</script>

<style scoped>
.panel-heading {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-input {
  max-width: 25rem;
}
</style>