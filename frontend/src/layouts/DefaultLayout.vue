<template>
  <v-app>
    <v-layout class="d-flex">
      <v-app-bar title="HEADER"></v-app-bar>

      <v-navigation-drawer>
        <v-list>
          <v-list-item 
          title="個人資産管理"
          to="/financial-simulation"  
          />
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
