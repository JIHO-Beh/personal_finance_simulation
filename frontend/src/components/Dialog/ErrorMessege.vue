<script setup lang="ts">
import { useErrorDialogStore } from "../../store/errorDialog";
import { storeToRefs } from 'pinia';

const errorDialogStore = useErrorDialogStore();
const { errorDialogIsOpen, errorDialogMessage } = storeToRefs(errorDialogStore);

</script>
<template>
  <v-dialog
    v-model="errorDialogIsOpen"
    width="auto"
  >
    <v-card
      max-width="600"
      min-width="400"
      :text="errorDialogMessage"
      title="エラー"
    > {{  }}
      <template v-slot:actions>
        <v-btn
          class="ms-auto"
          text="Ok"
          @click="errorDialogStore.closeErrorDialog()"
        ></v-btn>
      </template>
    </v-card>
  </v-dialog>
</template>