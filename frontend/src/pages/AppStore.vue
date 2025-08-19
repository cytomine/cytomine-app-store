<template>
  <div class="content-wrapper">
    <div class="panel">
      <h1 class="panel-heading is-flex is-justify-content-space-between is-align-items-center">
        {{ t('store') }}
      </h1>

      <div class="panel-block">
        <b-input class="search-input" v-model="searchString" icon="search" :placeholder="t('search')" />
      </div>

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
import { useI18n } from 'vue-i18n';

import AppCard from '@/components/app/AppCard.vue';
import { getAllTasks, searchTasks } from '@/api/tasks';
import type { App, Search } from '@/types/types.ts';

const { t } = useI18n();

const searchString = ref('');
const result = ref<Search[]>([]);
const tasks = ref<App[]>([]);

watch(searchString, async (query) => {
  if (!query || query.trim() === '') {
    tasks.value = await getAllTasks();
    result.value = [];
    return;
  }

  result.value = await searchTasks(query);

  tasks.value = tasks.value.filter(task =>
    result.value.some(item =>
      item.name === task.name &&
      item.namespace === task.namespace &&
      item.version === task.version
    )
  );
});

onMounted(async () => {
  tasks.value = await getAllTasks();
});

defineExpose({
  result,
  searchString,
  tasks,
});
</script>

<style scoped>
.search-input {
  max-width: 25rem;
}
</style>