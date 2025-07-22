<script setup lang="ts">
import PrimaryBtn from '../components/PrimaryBtn.vue';
import SimulationCard from '../components/SimulationCard.vue';
import { ref } from "vue";
import axios from 'axios'
import TooltipPlusIcon from '../components/TooltipPlusIcon.vue';

const SUPPORTED_COUNTRIES = "http://localhost:8083/api/v1/supported-countries"
const FINANIAL_SIMULAT = "http://localhost:8083/api/v1/supported-countries"

export interface MonthlyFixedPayment {
  monthlyFixedPaymentName: string;
  monthlyFixedPaymentAmount: number;
}
export interface SupportedCountries {
  countryCode: string;
  currency: string;
  countryName: string;
}
interface CardFormat {
  monthlyFixedPayment: MonthlyFixedPayment[]
}
const supportedCountries = ref()
const init = async () => {
  supportedCountries.value = await axios.get(SUPPORTED_COUNTRIES).then(data => {
    return data.data
  })
}
init()
const cardValues = ref<CardFormat[]>([])


const addMonthlyFixedPayment = () => {
  cardValues.value.push({monthlyFixedPayment: []});
  
}
const simurationEvent = async () => {
  await axios.get(FINANIAL_SIMULAT).then(data => {
    console.log(data)
  })
}
</script>

<template>
  <v-container>
    <v-row>
      <v-col :cols="4">
        <v-text-field
          hide-details="auto"
          label="目標金額"
          variant="underlined"
          color="indigo"
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
          hide-details="auto"
          label="給料"
          variant="underlined"
          color="indigo"
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
          :initial-monthly-fixed-payments="cardData.monthlyFixedPayment"
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
