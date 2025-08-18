<template>
  <b-navbar class="is-light">
    <template #brand>
      <b-navbar-item tag="router-link" to="/">
        <img src="https://raw.githubusercontent.com/buefy/buefy/dev/packages/docs/public/static/img/buefy-logo.png"
          alt="Cytomine App Store" />
      </b-navbar-item>
    </template>

    <template #start>
      <b-navbar-item tag="router-link" to="/my-apps">
        <i class="fas fa-code"></i>
        <span class="has-text-weight-semibold">{{ $t('my-apps') }}</span>
      </b-navbar-item>
    </template>
  </b-navbar>
</template>
