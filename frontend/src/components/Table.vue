<script setup lang="ts">
import { ref , defineProps, watch, type PropType } from 'vue';
import type { SimulationResult, SupportedCountries } from "../pages/FinancialSimulation.vue";

const props = defineProps({
  supportedCountries: {
    type: Array as PropType<SupportedCountries[]>,
    required: true,
    default: () => []
  },
  simulationResults: {
    type: Array as PropType<SimulationResult[]>,
    required: false,
    default: () => []
  }
});

const headers = ref<string[]>([])

watch(props.simulationResults, () => {
  headers.value = []
  console.log(props.simulationResults)
  let maxMonth = 0
  const currentMonth = new Date().getMonth()
  props.simulationResults.forEach((item: SimulationResult) => {
    maxMonth = item.monthsToGoal > maxMonth ? item.monthsToGoal : maxMonth
  })
  for (let i = 0; i < maxMonth; i++) {
    const monthIndex = (currentMonth + i) % 12;
    headers.value.push((monthIndex + 1) + "月");
  }
}, {
  deep: true,     // これが重要！配列内部の変更を監視します
  immediate: true // コンポーネントがマウントされた直後にも一度実行します
})

</script>

<template>
  <v-table striped="even">
    <thead>
      <tr>
        <th>国情報</th>
        <th v-for="item in headers"
          :key="item">{{ item }}</th>
        </tr>
    </thead>
    <tbody>
      <tr
        v-for="(simulationResult) in props.simulationResults"
      >
        <td>{{ props.supportedCountries.find((country: SupportedCountries)=> {
          return simulationResult.countryCode === country.countryCode
        })?.countryName }}</td>
        <div
          v-if="simulationResult.monthsToGoal === -1"
        >
          赤字のために積立金額がありません。
        </div>
          <td 
            v-else
            v-for="(item, index) in headers"
            :key="index">
            {{ (simulationResult.netMonthlySaving * (index + 1)).toFixed(2) }}
          </td>
      </tr>
    </tbody>
  </v-table>
</template>