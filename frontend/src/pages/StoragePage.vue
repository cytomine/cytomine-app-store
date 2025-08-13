<template>
  <div class="content-wrapper">
    <div class="panel">
      <p class="panel-heading">
        {{ $t('storage') }}
        <b-button>{{ $t('upload') }}</b-button>
      </p>

      <b-input class="search-input" v-model="searchString" icon="search" :placeholder="$t('search')" />

      <div class="panel-block">
        <AppCard :app="{}" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

import AppCard from '@/components/app/AppCard.vue';

const searchString = ref('');
</script>

<style scoped>
.panel-heading {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-input {
  max-width: 25rem;
}
</style>