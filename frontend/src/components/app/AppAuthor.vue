<template>
  <article class="media box author-box">
    <div class="media-content">
      <div class="content">
        <p>
          <strong>{{ fullName }}</strong>
        </p>
        <p class="has-text-grey mb-1">
          <i class="fas fa-envelope"></i>
          {{ author.email }}
        </p>
        <p class="has-text-grey">
          <i class="fas fa-building"></i>
          {{ author.organization }}
        </p>
      </div>
    </div>
  </article>
</template>

<script setup lang="ts">
import { computed } from "vue";
import type { Author } from "@/types/types";

const { author } = defineProps<{
  author: Author;
}>();

const fullName = computed(() => {
  return [author.firstName, author.lastName].join(" ");
});
</script>

<style scoped>
.author-box {
  height: 7.5rem;
  width: 20rem;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  margin: 0.5rem;
}
</style>
