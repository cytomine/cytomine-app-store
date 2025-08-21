<template>
  <div class="content-wrapper">
    <div class="panel">
      <p class="panel-heading is-flex is-justify-content-space-between is-align-items-center">
        <span>{{ t('my-apps') }}</span>
        <b-upload v-model="selectedFile" @update:modelValue="handleFileChange">
          <a class="button is-primary">{{ t('upload') }}</a>
        </b-upload>
      </p>

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
import { ref } from 'vue';
import { useI18n } from 'vue-i18n';

import AppCard from '@/components/app/AppCard.vue';
import { createTask } from '@/api/tasks';
import { useTask } from '@/composables/useTask';

const { t } = useI18n();
const { searchString, tasks } = useTask();

const selectedFile = ref<File | null>(null);
const handleFileChange = async () => {
  if (!selectedFile.value) {
    return;
  }

  const formData = new FormData();
  formData.append('task', selectedFile.value);

  try {
    tasks.value.push(await createTask(formData));
  } catch (error) {
    console.error('Upload error:', error);
  } finally {
    selectedFile.value = null;
  }
};

defineExpose({
  handleFileChange,
  selectedFile,
  tasks,
});
</script>

<style scoped>
.search-input {
  max-width: 25rem;
}
</style>