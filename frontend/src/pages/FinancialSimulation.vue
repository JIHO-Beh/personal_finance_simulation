<script setup lang="ts">
import PrimaryBtn from '../components/PrimaryBtn.vue';
import SimulationCard from '../components/SimulationCard.vue';
import { ref } from "vue";
import axios from 'axios'
import TooltipPlusIcon from '../components/TooltipPlusIcon.vue';
import Table from '../components/Table.vue';
import { useField, useForm } from 'vee-validate';
import yup from '../../yup.locale';

const SUPPORTED_COUNTRIES = "/api/v1/supported-countries"
const FINANCIAL_SIMULATION = "/api/v1/financial-simulation"

export interface SimulationResult {
  countryCode: string;
  monthsToGoal: number;
  netMonthlySaving: number
}
export interface MonthlyFixedPayment {
  monthlyFixedPaymentName: string;
  monthlyFixedPaymentAmount: number;
}
export interface SupportedCountries {
  countryCode: string;
  currency: string;
  countryName: string;
}
export interface CardFormat {
  countryCode: string;
  monthlyFixedPayment: MonthlyFixedPayment[]
  afterTaxSalary?: number
  netMonthlySaving?: number
}

// goalAmount: goalAmount.value,
//     salary: salary.value,
//     scenarios: cardValues.value.map((item: CardFormat) => {
//       return {
//         countryCode: item.countryCode,
//         fixedExpense: item.monthlyFixedPayment.map((monthlyFixedPayment: MonthlyFixedPayment) => {
//           return monthlyFixedPayment.monthlyFixedPaymentAmount
//         })
//       }
//     })
const monthlyFixedPaymentSchema = yup.object({
  monthlyFixedPaymentName: yup.string(), // 名前が必須
  monthlyFixedPaymentAmount: yup
    .number()
    .min(1, '固定費の金額は0以上である必要があります') // 金額は0以上
    .required('固定費の金額は必須です') // 金額が必須
    .typeError('固定費の金額は数値である必要があります'), // 型エラーメッセージ
});
const cardFormatSchema = yup.object({
  countryCode: yup.string().required('国コードは必須です'), // 国コードが必須
  monthlyFixedPayment: yup
    .array(monthlyFixedPaymentSchema),
  afterTaxSalary: yup.number().optional().nullable(),
  netMonthlySaving: yup.number().optional().nullable(),
  });

const validationSchema = yup.object({
  goalAmount: yup.number().min(0).required("目標金額を入力してください"),
  salary: yup
    .number()
    .min(0)
    .required(),
  cardValues: yup.array(cardFormatSchema)
})
// useFormフックでフォームの値、送信処理、エラーメッセージを管理
const { handleSubmit, errors } = useForm({
  validationSchema,
  initialValues: {
    goalAmount: 0, // または適切な初期値
    salary: 0,     // または適切な初期値
    cardValues: [] // ここが重要！空の配列で初期化します
  }
});

const { value: goalAmount } = useField<string>('goalAmount');
const { value: salary } = useField<string>('salary');
const { value: cardValues } = useField<CardFormat[]>('cardValues')
const values = ref({
  goalAmount,
  salary,
  cardValues
});

const onSubmit = handleSubmit(async values => {
  console.log(values)
  simulationResults.value = []
  const postData = {
    goalAmount: goalAmount.value,
    salary: salary.value,
    scenarios: cardValues.value.map((item: CardFormat) => {
      return {
        countryCode: item.countryCode,
        fixedExpense: item.monthlyFixedPayment.map((monthlyFixedPayment: MonthlyFixedPayment) => {
          return monthlyFixedPayment.monthlyFixedPaymentAmount
        })
      }
    })
  }
  await axios.post(FINANCIAL_SIMULATION, postData).then(response => {
    response.data.forEach((element: any, index: number) => {
      if(cardValues.value[index]){
        cardValues.value[index].afterTaxSalary = element.afterTaxSalary || 0
        cardValues.value[index].netMonthlySaving = element.netMonthlySaving|| 0
      }
      simulationResults.value.push({
        countryCode: element.countryCode,
        monthsToGoal: element.monthsToGoal,
        netMonthlySaving: element.netMonthlySaving
      })
    });
  })
  console.log(simulationResults.value.length)
});
const supportedCountries = ref()
const init = async () => {
  supportedCountries.value = await axios.get(SUPPORTED_COUNTRIES).then(data => {
    return data.data
  })
}
init()
const simulationResults = ref<SimulationResult[]>([])


const addMonthlyFixedPayment = () => {
  cardValues.value.push({
    countryCode: '',
    monthlyFixedPayment: [],
  });
  
}
</script>

<template>
  <v-container>
    <v-row>
      <v-col :cols="4">
        <v-text-field
          v-model="values.goalAmount"
          :error-messages="errors.goalAmount"
          hide-details="auto"
          label="目標金額"
          variant="underlined"
          color="indigo"
          type="number"
        ></v-text-field>
      </v-col>
      <v-col>
        <PrimaryBtn
          :btnText="'計算'" as string
          @child-event="onSubmit"
        />
      </v-col>
    </v-row>
    <v-row>
      <v-col :cols="4">
        <v-text-field
          v-model="values.salary"
          :error-messages="errors.salary"
          hide-details="auto"
          label="給料"
          variant="underlined"
          color="indigo"
          type="number"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-divider class="border-opacity-100 my-5" color="indigo"></v-divider>
      </v-col>
    </v-row>
    <v-row>
      <template v-for="(cardData, index) in values.cardValues" :key="cardData">
        <SimulationCard
          :card-information="cardData"
          :supported-countries="supportedCountries"
          :card-index="index"
          class="mb-4"
        />
      </template>
      <v-col cols="2" offset="2">
        <TooltipPlusIcon
          tooltip-text="押下時、比較対象欄が追加される"
          v-on:child-event="addMonthlyFixedPayment"
        />
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-divider class="border-opacity-100 my-5" color="indigo"></v-divider>
      </v-col>
    </v-row>
    <Table
      v-if="simulationResults.length"
      :simulation-results="simulationResults"
      :supported-countries="supportedCountries"
    />
  </v-container>
</template>

<style scoped>
.logo {
  height: 6em;
  padding: 1.5em;
  will-change: filter;
  transition: filter 300ms;
}
.logo:hover {
  filter: drop-shadow(0 0 2em #646cffaa);
}
.logo.vue:hover {
  filter: drop-shadow(0 0 2em #42b883aa);
}
</style>
