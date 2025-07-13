<script setup lang="ts">
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
  tooltipText: {
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
  <v-tooltip :text="props.tooltipText" location="top">
    <template v-slot:activator="{ props }">
      <v-icon
        v-bind="props"
        size="x-large"
        icon="mdi-plus-circle"
        color="indigo"
        @click="emitEvent"
      />
    </template>
  </v-tooltip>
</template>