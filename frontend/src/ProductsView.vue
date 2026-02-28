<script setup>
import { onMounted, ref } from "vue";
import { productImage } from "./productImages";
import { getRawMaterials, getProducts, createProduct } from "./api";

const rawMaterials = ref([]);
const products = ref([]);

const form = ref({
  code: "",
  name: "",
  price: "",
});

const components = ref([]); // { rawMaterialId, quantityRequired }

async function load() {
  rawMaterials.value = await getRawMaterials();
  products.value = await getProducts();
}

function addComponentRow() {
  components.value.push({ rawMaterialId: "", quantityRequired: "" });
}

function removeComponentRow(index) {
  components.value.splice(index, 1);
}

async function submit() {
  await createProduct({
    code: form.value.code,
    name: form.value.name,
    price: Number(form.value.price),
    components: components.value.map((c) => ({
      rawMaterialId: Number(c.rawMaterialId),
      quantityRequired: Number(c.quantityRequired),
    })),
  });

  form.value.code = "";
  form.value.name = "";
  form.value.price = "";
  components.value = [];

  await load();
}

onMounted(load);
</script>
<template>
  <div>
    <div class="card-title">
      <div>
        <h1>Produtos</h1>
        <div class="subtle">Cadastre produtos e sua composição (BOM) para cálculo de produção.</div>
      </div>
    </div>

    <div class="card mt-16">
      <div class="card-title">
        <h2>Novo Produto</h2>
        <span class="badge primary">Composição</span>
      </div>

      <form @submit.prevent="submit">
        <div class="form-row">
          <input v-model="form.code" class="input" placeholder="Código (ex: P-BALDE)" required />
          <input v-model="form.name" class="input" placeholder="Nome do produto" required />
          <input v-model="form.price" class="input" type="number" step="0.01" placeholder="Preço (R$)" required />

          <div class="actions">
            <button class="btn" type="button" @click="addComponentRow">+ Componente</button>
            <button class="btn primary" type="submit">Cadastrar Produto</button>
          </div>
        </div>

        <div class="mt-16">
          <h3>Composição</h3>

          <div v-if="components.length === 0" class="subtle mt-12">
            Adicione pelo menos 1 componente para o produto.
          </div>

          <div
            v-for="(c, idx) in components"
            :key="idx"
            class="form-row mt-12"
          >
            <select v-model="c.rawMaterialId" class="select" required>
              <option value="" disabled>Selecione a matéria-prima</option>
              <option v-for="rm in rawMaterials" :key="rm.id" :value="rm.id">
                {{ rm.code }} - {{ rm.name }}
              </option>
            </select>

            <input
              v-model="c.quantityRequired"
              class="input"
              type="number"
              step="0.0001"
              placeholder="Qtd por unidade"
              required
            />

            <button class="btn danger" type="button" @click="removeComponentRow(idx)">
              Remover
            </button>
          </div>
        </div>
      </form>
    </div>

    <div class="card mt-16">
      <div class="card-title">
        <h2>Catálogo de Produtos</h2>
        <span class="badge success">{{ products.length }} itens</span>
      </div>

      <div class="grid mt-12">
        <div v-for="p in products" :key="p.id" class="product-card">
          <img class="product-img" :src="productImage(p.code)" :alt="p.name" />
          <div class="product-body">
            <div class="product-title">
              <div>
                <div class="product-code">{{ p.code }}</div>
                <div class="product-name">{{ p.name }}</div>
              </div>
              <div class="product-price">R$ {{ p.price }}</div>
            </div>

            <div class="subtle mt-12">Composição</div>
            <ul class="components">
              <li v-for="c in p.components" :key="c.rawMaterialId">
                <span>{{ c.rawMaterialCode }}</span>
                <span class="muted">{{ c.quantityRequired }} / un</span>
              </li>
            </ul>
          </div>
        </div>
      </div>

      <div v-if="products.length === 0" class="subtle mt-16">
        Nenhum produto cadastrado ainda.
      </div>
    </div>
  </div>
</template>