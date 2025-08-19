<template>
  <b-navbar class="is-light">
    <template #brand>
      <b-navbar-item tag="router-link" to="/">
        <img src="@/assets/logo.svg" id="logo" alt="Cytomine App Store" />
      </b-navbar-item>
    </template>

    <template #start>
      <b-navbar-item tag="router-link" to="/">
        <i class="fas fa-store"></i>
        <span class="has-text-weight-semibold">{{ $t('store') }}</span>
      </b-navbar-item>
      <b-navbar-item tag="router-link" to="/my-apps">
        <i class="fas fa-code"></i>
        <span class="has-text-weight-semibold">{{ $t('my-apps') }}</span>
      </b-navbar-item>
    </template>
  </b-navbar>
</template>

<style scoped>
#logo {
  height: 2rem;
}
</style>
