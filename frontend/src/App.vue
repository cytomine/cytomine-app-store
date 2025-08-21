<template>
  <main>
    <AppNavbar />
    <RouterView />
  </main>
</template>

<script setup lang="ts">
import AppNavbar from '@/components/navbar/AppNavbar.vue';
</script>

<style>
.content-wrapper {
  padding: 1.5% 2.5%;
  position: relative;
  min-height: 100%;
}
</style>