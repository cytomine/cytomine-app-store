import { ref } from 'vue';
import { defineStore } from 'pinia';

import { getTask } from '@/api/tasks';
import type { App } from '@/types/types';

export const useTaskStore = defineStore('tasks', () => {
  const tasks = ref<Map<string, App>>(new Map());
  const isLoading = ref(false);
  const error = ref<string | null>(null);

  const getTaskKey = (namespace: string, version: string) => `${namespace}:${version}`;

  const fetchTask = async (namespace: string, version: string) => {
    const key = getTaskKey(namespace, version);

    if (tasks.value.has(key)) {
      return tasks.value.get(key)!;
    }

    try {
      isLoading.value = true;
      const task = await getTask(namespace, version);
      tasks.value.set(key, task);
      return task;
    } catch (err) {
      error.value = (err as Error).message;
      throw err;
    } finally {
      isLoading.value = false;
    }
  };

  return { tasks, isLoading, error, fetchTask };
});