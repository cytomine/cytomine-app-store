<template>
  <AppNavbar />
  <h1 class="has-text-warning">{{ $t('hello-world') }}</h1>
  <p>
    Visit <a href="https://vuejs.org/" target="_blank" rel="noopener">vuejs.org</a> to read the
    documentation
  </p>
  <p class="mb-6">Margin bottom</p>
  <b-button class="mb-2 is-primary has-text-grey is-size-2 is-align-content-end">
    <b-icon icon="plus" /> Click Me
  </b-button>
</template>

<script setup lang="ts">
import AppNavbar from '@/components/navbar/AppNavbar.vue';
</script>