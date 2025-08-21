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
import { useI18n } from 'vue-i18n';

import AppCard from '@/components/app/AppCard.vue';
import { useTask } from '@/composables/useTask';

const { t } = useI18n();
const { searchString, result, tasks } = useTask();

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