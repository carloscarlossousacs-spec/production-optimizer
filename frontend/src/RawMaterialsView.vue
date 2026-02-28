<script setup>
import { onMounted, ref, computed } from "vue";
import { getRawMaterials, createRawMaterial } from "./api";

const materials = ref([]);

const form = ref({
  code: "",
  name: "",
  stockQuantity: ""
});

async function loadMaterials() {
  materials.value = await getRawMaterials();
}

async function submit() {
  await createRawMaterial({
    code: form.value.code,
    name: form.value.name,
    stockQuantity: Number(form.value.stockQuantity)
  });

  form.value.code = "";
  form.value.name = "";
  form.value.stockQuantity = "";

  await loadMaterials();
}

onMounted(loadMaterials);

// ---- dashboard metrics ----
const totalMaterials = computed(() => materials.value.length);

const totalStock = computed(() =>
  materials.value.reduce((acc, m) => acc + Number(m.stockQuantity || 0), 0)
);

const maxStock = computed(() =>
  Math.max(0, ...materials.value.map(m => Number(m.stockQuantity || 0)))
);

function stockPct(qty) {
  const max = maxStock.value || 1;
  return Math.max(0, Math.min(100, (Number(qty) / max) * 100));
}

function statusOf(qty) {
  const n = Number(qty || 0);
  if (n <= 0) return { label: "Sem estoque", cls: "danger" };
  if (n < 50) return { label: "Baixo", cls: "warn" };
  return { label: "OK", cls: "success" };
}
function formatNumber(value) {
  return new Intl.NumberFormat("pt-BR", {
    minimumFractionDigits: 0,
    maximumFractionDigits: 4,
  }).format(value);
}
</script>

<template>
  <div>
    <div class="card-title">
      <div>
        <h1>Matérias-Primas</h1>
        <div class="subtle">Visão de estoque e cadastro rápido para indústria do plástico.</div>
      </div>
      <button class="btn" @click="loadMaterials">Atualizar</button>
    </div>

    <!-- DASHBOARD METRICS -->
    <div class="metrics mt-16">
      <div class="metric-card">
        <div class="metric-label">Itens cadastrados</div>
        <div class="metric-value">{{ formatNumber(totalMaterials) }}</div>
        <div class="metric-subtle">Matérias-primas no sistema</div>
      </div>

      <div class="metric-card">
        <div class="metric-label">Estoque total</div>
        <div class="metric-value">{{ formatNumber(totalStock) }}</div>
        <div class="metric-subtle">Soma das quantidades</div>
      </div>

      <div class="metric-card">
        <div class="metric-label">Maior estoque</div>
        <div class="metric-value">{{ formatNumber(maxStock)}}</div>
        <div class="metric-subtle">Referência para barras</div>
      </div>
    </div>

    <!-- CREATE FORM -->
    <div class="card mt-16">
      <div class="card-title">
        <h2>Nova Matéria-Prima</h2>
        <span class="badge primary">Cadastro</span>
      </div>

      <form @submit.prevent="submit">
        <div class="form-row">
          <input v-model="form.code" class="input" placeholder="Código (ex: RM-PE)" required />
          <input v-model="form.name" class="input" placeholder="Nome (ex: Polietileno)" required />
          <input
            v-model="form.stockQuantity"
            class="input"
            type="number"
            step="0.0001"
            placeholder="Estoque"
            required
          />

          <div class="actions">
            <button class="btn primary" type="submit">Cadastrar</button>
          </div>
        </div>
      </form>
    </div>

    <!-- TABLE -->
    <div class="card mt-16">
      <div class="card-title">
        <h2>Estoque Atual</h2>
        <span class="badge success">{{formatNumber(materials.length)}} itens</span>
      </div>

      <table class="table mt-12" v-if="materials.length > 0">
        <thead>
          <tr>
            <th>Código</th>
            <th>Matéria-prima</th>
            <th>Estoque</th>
            <th>Nível</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="m in materials" :key="m.id">
            <td><b>{{ m.code }}</b></td>
            <td>{{ m.name }}</td>
            <td>{{ formatNumber(m.stockQuantity) }}</td>
            <td style="width: 220px;">
              <div class="bar">
                <div class="bar-fill" :style="{ width: stockPct(m.stockQuantity) + '%' }"></div>
              </div>
              <div class="bar-subtle">{{ formatNumber(stockPct(m.stockQuantity).toFixed(0)) }}%</div>
            </td>
            <td>
              <span class="pill" :class="statusOf(m.stockQuantity).cls">
                {{ statusOf(m.stockQuantity).label }}
              </span>
            </td>
          </tr>
        </tbody>
      </table>

      <div v-else class="subtle mt-16">
        Nenhuma matéria-prima cadastrada ainda.
      </div>
    </div>
  </div>
</template>