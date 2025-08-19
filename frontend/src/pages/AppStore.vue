<template>
  <div class="content-wrapper">
    <div class="panel">
      <p class="panel-heading is-flex is-justify-content-space-between is-align-items-center">
        <span>{{ $t('store') }}</span>
      </p>

      <div class="panel-block">
        <b-input class="search-input" v-model="searchString" icon="search" :placeholder="$t('search')" />
      </div>

      <p>{{ result }}</p>

      <div class="panel-block">
        <div class="columns is-multiline">
          <div class="column" v-for="task in tasks" :key="task.id">
            <AppCard :app="task" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';

import AppCard from '@/components/app/AppCard.vue';
import { getAllTasks, searchTask } from '@/api/tasks';
import type { App } from '@/types/types.ts';

const searchString = ref('');
const result = ref('');
const tasks = ref<App[]>([]);

watch(searchString, async (query) => {
  if (!query || query.trim() === '') {
    return;
  }

  result.value = await searchTask(query);
});

onMounted(async () => {
  tasks.value = await getAllTasks();
});

defineExpose({
  tasks,
});
</script>

<style scoped>
.search-input {
  max-width: 25rem;
}
</style>