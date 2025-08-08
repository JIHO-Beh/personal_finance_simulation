import { ref } from "vue";
import { defineStore } from "pinia";

export const useErrorDialogStore = defineStore(
  "dialog",
  () => {
    const errorDialogIsOpen = ref<boolean>(false)
    const errorDialogMessage = ref<string>('')

    const openErrorDialog = (errorMessage: string) => {
      errorDialogIsOpen.value = true
      errorDialogMessage.value = errorMessage
    }

    const closeErrorDialog = () => {
      errorDialogIsOpen.value = false
      errorDialogMessage.value = ''
    }

    return {
      errorDialogIsOpen,
      errorDialogMessage,
      openErrorDialog,
      closeErrorDialog
    }
  },
);