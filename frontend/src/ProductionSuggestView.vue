<script setup>
import { ref } from "vue";
import { getProductionSuggestion } from "./api";
import { productImage } from "./productImages";

const suggestion = ref(null);
const loading = ref(false);

async function loadSuggestion() {
  loading.value = true;
  try {
    suggestion.value = await getProductionSuggestion();
  } finally {
    loading.value = false;
  }
}

function toNumber(v) {
  const n = Number(v);
  return Number.isFinite(n) ? n : 0;
}

function formatMoney(value) {
  return new Intl.NumberFormat("pt-BR", {
    style: "currency",
    currency: "BRL",
  }).format(toNumber(value));
}
</script>

<template>
  <div>
    <div class="card-title">
      <div>
        <h1>Sugestão de Produção</h1>
        <div class="subtle">Planejamento otimizado com base no estoque disponível.</div>
      </div>

      <button class="btn primary" @click="loadSuggestion">
        Calcular Produção Ideal
      </button>
    </div>

    <!-- LOADING -->
    <div v-if="loading" class="card mt-16">
      <div class="subtle">Calculando melhor cenário...</div>
    </div>

    <!-- RESULT -->
    <div v-if="suggestion && !loading" class="mt-16">
      <!-- TOTAL CARD -->
      <div class="card total-card">
        <div>
          <div class="metric-label">Receita Total Estimada</div>
          <div class="total-value">
            {{ formatMoney(suggestion.totalValue) }}
          </div>
          <div class="subtle mt-12">
            Produtos sugeridos: <b>{{ suggestion.items?.length ?? 0 }}</b>
          </div>
        </div>
      </div>

      <!-- PRODUCT CARDS -->
      <div class="grid mt-16">
        <div
          v-for="(item, index) in suggestion.items"
          :key="item.productId"
          class="product-card suggest-card"
        >
          <img
            class="product-img"
            :src="productImage(item.productCode)"
            :alt="item.productName"
          />

          <div class="product-body">
            <div class="product-title">
              <div>
                <div class="product-code">
                  #{{ index + 1 }} — {{ item.productCode }}
                </div>
                <div class="product-name">
                  {{ item.productName }}
                </div>
              </div>

              <span class="badge success">
                x{{ item.quantityToProduce }}
              </span>
            </div>

            <div class="mt-12">
              <span class="revenue">
                {{ formatMoney(item.totalValue) }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- EMPTY STATE -->
    <div v-if="!suggestion && !loading" class="card mt-16">
      <div class="subtle">
        Clique em "Calcular Produção Ideal" para gerar a sugestão.
      </div>
    </div>
  </div>
</template>

