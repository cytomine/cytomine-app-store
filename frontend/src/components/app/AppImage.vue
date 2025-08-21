<template>
  <img alt="App Image" :src="imageUrl" @error="handleImageError" />
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue';

const { namespace, version } = defineProps<{
  namespace?: string,
  version?: string,
  ratio?: string,
}>();

const fallbackUrl = 'https://bulma.io/assets/images/placeholders/1280x960.png';
const hasImageError = ref(false);

const imageUrl = computed(() => {
  if (hasImageError.value) {
    return fallbackUrl;
  }

  const baseUrl = import.meta.env.VITE_API_BASE_URL;

  if (!baseUrl || !namespace || !version) {
    return fallbackUrl;
  }

  return `${baseUrl}/api/v1/tasks/${namespace}/${version}/logo`;
});

watch(
  () => [namespace, version],
  () => {
    hasImageError.value = false;
  },
);

const handleImageError = (event: Event) => {
  const target = event.target as HTMLImageElement;

  // Prevent infinite loop if fallback also fails
  if (target.src !== fallbackUrl) {
    hasImageError.value = true;
  }
};

defineExpose({
  fallbackUrl,
});
</script>
