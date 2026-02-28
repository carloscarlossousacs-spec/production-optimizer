<template>
  <div>
    <header class="app-header">
      <div class="header-left">
        <div class="logo">⚙️</div>
        <div class="brand">
          <div class="brand-title">Production Optimizer</div>
          <div class="brand-sub">Industrial Planning System</div>
        </div>
      </div>

      <nav class="nav">
        <button 
          :class="['nav-btn', route === 'raw' ? 'active' : '']"
          @click="route = 'raw'"
        >
          Matérias-Primas
        </button>

        <button 
          :class="['nav-btn', route === 'products' ? 'active' : '']"
          @click="route = 'products'"
        >
          Produtos
        </button>

        <button 
          :class="['nav-btn', route === 'suggest' ? 'active' : '']"
          @click="route = 'suggest'"
        >
          Sugestão
        </button>
      </nav>
    </header>

    <main class="app-content">
      <RawMaterialsView v-if="route === 'raw'" />
      <ProductsView v-if="route === 'products'" />
      <ProductionSuggestView v-if="route === 'suggest'" />
    </main>
  </div>
</template>

<script setup>
import { ref } from "vue";
import RawMaterialsView from "./RawMaterialsView.vue";
import ProductsView from "./ProductsView.vue";
import ProductionSuggestView from "./ProductionSuggestView.vue";

const route = ref("raw");
</script>