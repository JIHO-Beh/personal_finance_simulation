<script setup lang="ts">
import PrimaryBtn from '../components/PrimaryBtn.vue';
import SimulationCard from '../components/SimulationCard.vue';
import { ref } from "vue";
import axios from 'axios'

const SUPPORTED_COUNTRIES = "/api/v1/supported-countries"
const FINANIAL_SIMULAT = "/api/v1/financial-simulat"

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

const init = async () => {
  await axios.get(SUPPORTED_COUNTRIES).then(data => {
    console.log(data)
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
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <template v-for="(cardData, index) in cardValues" :key="index">
        <SimulationCard
          :card-data="cardData"
          class="mb-4"
        />
      </template>
      <v-col cols="2" offset="2">
        <v-btn 
          variant="outlined"
          size="large"
          icon="mdi-plus"
          base-color="private"
          @click="addMonthlyFixedPayment"
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
