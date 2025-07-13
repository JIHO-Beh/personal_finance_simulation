<script setup lang="ts">
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
  btnText: {
    type: String,
    required: true,
  },
});

const emit = defineEmits(['child-event']);
const emitEvent = () => {
  emit('child-event'); // 'child-event' 이름으로 이벤트 발생
};
</script>

<template>
  <v-btn
    class="ma-3"
    variant="elevated"
    color="secondary"
    @click="emitEvent"
  >
    {{props.btnText}}
  </v-btn>
</template>