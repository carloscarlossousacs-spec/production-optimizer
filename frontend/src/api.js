const BASE_URL = "http://localhost:8080/api";

// RAW MATERIALS
export async function getRawMaterials() {
  const res = await fetch(`${BASE_URL}/raw-materials`);
  return res.json();
}

export async function createRawMaterial(data) {
  const res = await fetch(`${BASE_URL}/raw-materials`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  return res.json();
}

// PRODUCTS
export async function getProducts() {
  const res = await fetch(`${BASE_URL}/products`);
  return res.json();
}

export async function createProduct(data) {
  const res = await fetch(`${BASE_URL}/products`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  return res.json();
}

// PRODUCTION
export async function getProductionSuggestion() {
  const res = await fetch(`${BASE_URL}/production/suggest`);
  return res.json();
}