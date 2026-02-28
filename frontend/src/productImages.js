import balde from "./assets/products/p-balde.png";
import caixa from "./assets/products/p-caixa.png";
import garrafa from "./assets/products/p-garrafa.png";
import cadeira from "./assets/products/p-cadeira.png";
import tampa from "./assets/products/p-tampa.png";
import premium from "./assets/products/p-premium.png";
import placeholder from "./assets/products/placeholder.png";

const byCode = {
  "P-BALDE": balde,
  "P-CAIXA": caixa,
  "P-GARRAFA": garrafa,
  "P-CADEIRA": cadeira,
  "P-TAMPA": tampa,
  "P-PREMIUM": premium,
};

export function productImage(code) {
  return byCode[code] ?? placeholder;
}