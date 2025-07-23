<script setup lang="ts">
import PrimaryBtn from '../components/PrimaryBtn.vue';
import SimulationCard from '../components/SimulationCard.vue';
import { ref } from "vue";
import axios from 'axios'
import TooltipPlusIcon from '../components/TooltipPlusIcon.vue';

const SUPPORTED_COUNTRIES = "/api/v1/supported-countries"
const FINANCIAL_SIMULATION = "/api/v1/financial-simulation"

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
}
const supportedCountries = ref()
const goalAmount = ref<number>()
const salary = ref<number>()
const init = async () => {
  supportedCountries.value = await axios.get(SUPPORTED_COUNTRIES).then(data => {
    return data.data
  })
}
init()
const cardValues = ref<CardFormat[]>([])


const addMonthlyFixedPayment = () => {
  cardValues.value.push({
    countryCode: '',
    monthlyFixedPayment: []
  });
  
}
const simurationEvent = async () => {
  // await axios.get(FINANCIAL_SIMULATION).then(data => {
  //   console.log(data)
  // })
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
    // 요청이 성공했을 때 실행될 코드
    console.log('POST リクエストが成功しました！');
    console.log('レスポンスデータ:', response.data); // 서버로부터 받은 응답 데이터
    console.log('ステータスコード:', response.status); // HTTP 상태 코드 (예: 200, 201)
  })
  .catch(error => {
    // 요청이 실패했을 때 실행될 코드
    console.error('POST リクエスト中にエラーが発生しました:', error);
    if (error.response) {
      // 서버가 응답했지만 상태 코드가 2xx 범위가 아닌 경우
      console.error('エラーレスポンスデータ:', error.response.data);
      console.error('エラーレスポンスステータス:', error.response.status);
      console.error('エラーレスポンスヘッダー:', error.response.headers);
    } else if (error.request) {
      // 요청이 전송되었지만 응답을 받지 못한 경우 (네트워크 문제 등)
      console.error('リクエストが送信されましたが、レスポンスがありませんでした:', error.request);
    } else {
      // 요청을 설정하는 중에 오류가 발생한 경우
      console.error('エラー:', error.message);
    }
  });
}
</script>

<template>
  <v-container>
    <v-row>
      <v-col :cols="4">
        <v-text-field
          v-model="goalAmount"
          hide-details="auto"
          label="目標金額"
          variant="underlined"
          color="indigo"
          type="number"
        ></v-text-field>
      </v-col>
      <v-col>
        <PrimaryBtn
          :btnText="'btnText'" as string
          @child-event="simurationEvent"
        />
      </v-col>
    </v-row>
    <v-row>
      <v-col :cols="4">
        <v-text-field
          v-model="salary"
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
      <template v-for="(cardData, index) in cardValues" :key="index">
        <SimulationCard
          :card-information="cardData"
          :supported-countries="supportedCountries"
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
