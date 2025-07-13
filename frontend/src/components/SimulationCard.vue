<script setup lang="ts">
import { ref , defineProps, defineEmits, type PropType } from 'vue';
import type { MonthlyFixedPayment, SupportedCountries } from "../pages/FinancialSimulation.vue";

const props = defineProps({
  initialMonthlyFixedPayments: {
    type: Array as PropType<MonthlyFixedPayment[]>,
    required: true,
    default: () => []
  },
  supportedCountries: {
    type: Array as PropType<SupportedCountries[]>,
    required: true,
    default: () => []
  }
});
const monthlyFixedPayments = ref<MonthlyFixedPayment[]>([
  ...props.initialMonthlyFixedPayments, 
]);
const supportedCountries = ref<SupportedCountries[]>([
  ...props.supportedCountries, 
]);
console.log(supportedCountries)
const emit = defineEmits(['update:monthlyFixedPayments']);

const addMonthlyFixedPayment = () => {
  monthlyFixedPayments.value.push({
    monthlyFixedPaymentName: '',
    monthlyFixedPaymentAmount: 0,
  });
  // 부모 컴포넌트에 변경된 배열을 emit할 수도 있습니다.
  emit('update:monthlyFixedPayments', monthlyFixedPayments.value);
};

// 각 입력 필드에서 값이 변경될 때 호출될 함수 (선택 사항: emit을 통해 부모에게 알림)
const updatePayment = () => {
  emit('update:monthlyFixedPayments', monthlyFixedPayments.value);
};

</script>

<template>
  <v-col cols="4">
    <v-card>
      <v-row class="ma-1">
        <v-col>
          <v-select
            label="国選択"
            :items="['韓国', '日本']"
          ></v-select>
        </v-col>
      </v-row>
      <v-row
        v-for="(payment, index) in monthlyFixedPayments"
        :key="index"
        class="ma-1"
      >
        <v-col cols="6">
          <v-text-field
            hide-details="auto"
            label="月固定支払名称"
            v-model="payment.monthlyFixedPaymentName"
            @update:modelValue="updatePayment"
          ></v-text-field>
        </v-col>
        <v-col cols="6">
          <v-text-field
            type="number"
            hide-details="auto"
            label="月固定支払額"
            v-model.number="payment.monthlyFixedPaymentAmount" 
            @update:modelValue="updatePayment"
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row class="ma-1">
        <v-col align="center">
          <v-btn
            density="compact"
            icon="mdi-plus"
            @click="addMonthlyFixedPayment"
          ></v-btn>
        </v-col>
      </v-row>
    </v-card>
  </v-col>
</template>