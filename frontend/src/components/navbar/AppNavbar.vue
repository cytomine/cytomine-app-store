<template>
  <b-navbar class="is-light">
    <template #brand>
      <b-navbar-item tag="router-link" to="/">
        <img src="https://raw.githubusercontent.com/buefy/buefy/dev/packages/docs/public/static/img/buefy-logo.png"
          alt="Cytomine App Store" />
      </b-navbar-item>
    </template>

    <template #start>
      <b-navbar-item tag="router-link" to="/storage">
        <i class="fas fa-download"></i>
        <span class="ml-2 has-text-weight-semibold">{{ $t('storage') }}</span>
      </b-navbar-item>
    </template>
  </b-navbar>
</template>
