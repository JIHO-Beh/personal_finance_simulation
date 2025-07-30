<script setup lang="ts">
import { ref , defineProps, defineEmits, type PropType } from 'vue';
import type { CardFormat, SupportedCountries, MonthlyFixedPayment } from "../pages/FinancialSimulation.vue";
import TooltipPlusIcon from './TooltipPlusIcon.vue';
import { useField } from 'vee-validate';

const props = defineProps({
  cardInformation: {
    type: Object as PropType<CardFormat>,
    required: true,
  },
  supportedCountries: {
    type: Array as PropType<SupportedCountries[]>,
    required: true,
    default: () => []
  },
  cardIndex: {
    type: Number,
    required: true,
  }
});
const { value: countryCodeValue, errorMessage: countryCodeError } = useField<string>(`cardValues[${props.cardIndex}].countryCode`);
const { value: monthlyFixedPaymentValue, errorMessage: monthlyFixedPaymentError } = 
useField<MonthlyFixedPayment[]>(`cardValues[${props.cardIndex}].monthlyFixedPayment`);
const currency = ref<string>()

const emit = defineEmits(['update:cardInformation']);

const addMonthlyFixedPayment = () => {
  props.cardInformation.monthlyFixedPayment.push({
    monthlyFixedPaymentName: '',
    monthlyFixedPaymentAmount: 0,
  });
  // 부모 컴포넌트에 변경된 배열을 emit할 수도 있습니다.
  emit('update:cardInformation', props.cardInformation);
};

// 각 입력 필드에서 값이 변경될 때 호출될 함수 (선택 사항: emit을 통해 부모에게 알림)
const updatePayment = () => {
  console.log(props.cardInformation)
  emit('update:cardInformation', props.cardInformation);
};
const handleSelectionChange = (newValue: string) => {
  const selectedCountryObject = props.supportedCountries.find(c => c.countryCode === newValue);
  if (selectedCountryObject) {
    currency.value = selectedCountryObject.currency;
  }
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
            v-model="countryCodeValue"
            :items="props.supportedCountries"
            label="国選択"
            item-title="countryName"
            item-value="countryCode"
            :error-messages="countryCodeError"
            @update:modelValue="handleSelectionChange"
          ></v-select>
        </v-col>
      </v-row>
      
      <div v-if="cardInformation.afterTaxSalary">
        <v-row>
          <v-col class="mx-7 my-0 pa-0">
            <v-divider class="border-opacity-100 my-5" color="indigo"></v-divider>
          </v-col>
        </v-row>
        <v-row class="ma-1">
          <v-col align="end">
            <h4>税込給料({{(currency ? currency : '')}}) : {{cardInformation.afterTaxSalary}}</h4>
          </v-col>
        </v-row>
      </div>
      <v-row class="ma-1">
        <v-col>
          <h4>月固定費設定</h4>
        </v-col>
      </v-row>
      <v-row
        v-for="(payment, index) in monthlyFixedPaymentValue"
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
            :label="`金額(${currency ? currency : ''})`"
            v-model.number="payment.monthlyFixedPaymentAmount" 
            :error-messages="monthlyFixedPaymentError"
            @update:modelValue="updatePayment"
            default="0"
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
      <v-row class="ma-1">
        <v-col align="end">
          <h4>固定費合計({{(currency ? currency : '')}})：{{ cardInformation.monthlyFixedPayment.reduce((sum: number, item: MonthlyFixedPayment) => sum + item.monthlyFixedPaymentAmount, 0) }}</h4>
        </v-col>
      </v-row>
      <div v-if="cardInformation.afterTaxSalary">
        <v-row>
          <v-col class="mx-7 my-0 pa-0">
            <v-divider class="border-opacity-100 my-5" color="indigo"></v-divider>
          </v-col>
        </v-row>
        <v-row class="ma-1">
          <v-col align="end">
            <h4>積み立て金額({{(currency ? currency : '')}}) : {{cardInformation.netMonthlySaving}}</h4>
          </v-col>
        </v-row>
      </div>
    </v-card>
  </v-col>
</template>