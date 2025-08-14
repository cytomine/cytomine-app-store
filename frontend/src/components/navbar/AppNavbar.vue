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
        {{ $t('storage') }}
      </b-navbar-item>
    </template>

    <template #end>
      <b-navbar-item tag="div">
        <div class="buttons">
          <a class="button is-primary">
            <strong>Sign up</strong>
          </a>
          <a class="button is-light"> Log in </a>
        </div>
      </b-navbar-item>
    </template>
  </b-navbar>
</template>
