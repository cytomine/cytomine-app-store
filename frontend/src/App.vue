<template>
  <div>
    <AppNavbar />
    <h1 class="has-text-warning">{{ $t('hello-world') }}</h1>
  </div>
</template>

<script setup lang="ts">
import AppNavbar from '@/components/navbar/AppNavbar.vue';
</script>

<style>
html,
body {
  height: 100vh;
  margin: 0;
  padding: 0;
  overflow: hidden;
}

body {
  font-family: "Open Sans", "Helvetica Neue", Helvetica, Arial, sans-serif, sans-serif;
  color: #333;
  background: #d4d4d4;
}
</style>