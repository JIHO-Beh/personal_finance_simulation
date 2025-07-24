<script setup lang="ts">
import { ref , defineProps, defineEmits, type PropType } from 'vue';
import type { CardFormat, SupportedCountries } from "../pages/FinancialSimulation.vue";
import TooltipPlusIcon from './TooltipPlusIcon.vue';

const props = defineProps({
  cardInformation: {
    type: Object as PropType<CardFormat>,
    required: true,
  },
  supportedCountries: {
    type: Array as PropType<SupportedCountries[]>,
    required: true,
    default: () => []
  }
});
const selectedCountry = ref<SupportedCountries>();
const currency = ref<string>()

const cardInformation = ref<CardFormat>(
  props.cardInformation, 
);
const supportedCountries = ref<SupportedCountries[]>([
  ...props.supportedCountries, 
]);
console.log(supportedCountries)
const emit = defineEmits(['update:cardInformation']);

const addMonthlyFixedPayment = () => {
  cardInformation.value.monthlyFixedPayment.push({
    monthlyFixedPaymentName: '',
    monthlyFixedPaymentAmount: 0,
  });
  // 부모 컴포넌트에 변경된 배열을 emit할 수도 있습니다.
  emit('update:cardInformation', cardInformation.value);
};

// 각 입력 필드에서 값이 변경될 때 호출될 함수 (선택 사항: emit을 통해 부모에게 알림)
const updatePayment = () => {
  emit('update:cardInformation', cardInformation.value);
};
const handleSelectionChange = (newValue: SupportedCountries) => {
  currency.value = newValue.currency
  props.cardInformation.countryCode = newValue.countryCode
};

</script>

<template>
  <v-col cols="4">
    <v-card
      color="indigo"
      variant="outlined"
    >
      <v-row class="ma-1">
        <v-col>
          <h2>比較対象</h2>
        </v-col>
      </v-row>
      <v-row class="ma-1">
        <v-col>
          <v-select
            v-model="selectedCountry"
            label="国選択"
            :items="supportedCountries"
            item-title="countryName"
            :return-object="true"  
            @update:modelValue="handleSelectionChange"
          ></v-select>
        </v-col>
      </v-row>
      <v-row class="ma-1">
        <v-col>
          月固定費設定
        </v-col>
      </v-row>
      <v-row
        v-for="(payment, index) in cardInformation.monthlyFixedPayment"
        :key="index"
        class="ma-1"
      >
        <v-col cols="6">
          <v-text-field
            hide-details="auto"
            label="項目名"
            v-model="payment.monthlyFixedPaymentName"
            @update:modelValue="updatePayment"
          ></v-text-field>
        </v-col>
        <v-col cols="6">
          <v-text-field
            type="number"
            hide-details="auto"
            :label="`金額${currency ? currency : ''}`"
            v-model.number="payment.monthlyFixedPaymentAmount" 
            @update:modelValue="updatePayment"
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row class="ma-1">
        <v-col align="center">
          <TooltipPlusIcon
            tooltip-text="押下時、月固定費設定欄が追加される"
            v-on:child-event="addMonthlyFixedPayment"
          />
        </v-col>
      </v-row>
    </v-card>
  </v-col>
</template>