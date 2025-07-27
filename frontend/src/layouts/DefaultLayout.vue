<template>
  <v-app>
    <v-layout class="d-flex">
      <v-app-bar color="primary" title="個人資産管理"/>

      <v-navigation-drawer color="secondary">
        <v-list>
          <v-list-item 
            title=""
            to="/financial-simulation"
            color="white"
          >
            個人資産管理
          </v-list-item>
        </v-list>
      </v-navigation-drawer>

      <v-main class="d-flex justify-center">
        <RouterView />
      </v-main>
    </v-layout>
  </v-app>
</template>

<script setup lang="ts">
</script>

<style scoped></style>
