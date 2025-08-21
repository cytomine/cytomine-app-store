import { onMounted, ref, watch } from 'vue';

import { getAllTasks, getTaskBundle, searchTasks } from '@/api/tasks';
import type { App, Search } from '@/types/types';

export function useTask() {
  const searchString = ref('');
  const result = ref<Search[]>([]);
  const tasks = ref<App[]>([]);
  const isLoading = ref(false);

  const downloadTask = async (namespace: string, version: string) => {
    try {
      const blobData = await getTaskBundle(namespace, version);
      const blob = new Blob([blobData], { type: 'application/zip' });
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = `${namespace}-${version}.zip`;
      link.click();
      URL.revokeObjectURL(link.href);
    } catch (error) {
      console.error('Download failed:', error);
      alert('Failed to download file.');
    }
  };

  const search = async (query: string) => {
    if (!query || query.trim() === '') {
      tasks.value = await getAllTasks();
      result.value = [];
      return;
    }

    isLoading.value = true;
    try {
      result.value = await searchTasks(query);

      tasks.value = tasks.value.filter(task =>
        result.value.some(item =>
          item.name === task.name &&
          item.namespace === task.namespace &&
          item.version === task.version,
        ),
      );
    } finally {
      isLoading.value = false;
    }
  };

  const setupTasks = async () => {
    isLoading.value = true;
    try {
      tasks.value = await getAllTasks();
    } finally {
      isLoading.value = false;
    }
  };

  watch(searchString, search);

  onMounted(setupTasks);

  return {
    downloadTask,
    searchString,
    result,
    tasks,
    isLoading,
  };
}
