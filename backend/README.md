# Production Optimizer

Sistema de Otimização de Produção Industrial para Indústria do Plástico.

Esta aplicação permite o gerenciamento de matérias-primas e produtos,
além de sugerir automaticamente o melhor plano de produção para
maximizar o faturamento com base no estoque disponível.

------------------------------------------------------------------------

# FUNCIONALIDADES

Gerenciamento de Matérias-Primas - Cadastro de matérias-primas -
Listagem de matérias-primas - Atualização de estoque

Gerenciamento de Produtos - Cadastro de produtos - Definição de
composição (matérias-primas + quantidade necessária) - Listagem de
produtos

Otimização de Produção - Calcula a melhor estratégia de produção -
Maximiza o valor total de venda - Resolve conflitos de uso de
matéria-prima - Sugere quantidade ideal de fabricação por produto

------------------------------------------------------------------------

# TECNOLOGIAS UTILIZADAS

Backend - Java 17 - Spring Boot 3 - Spring Data JPA - Banco H2 - Maven

Frontend - Vue 3 - Vite - Fetch API

------------------------------------------------------------------------

# ESTRUTURA DO PROJETO

production-optimizer/ ├── backend/ └── frontend/

------------------------------------------------------------------------

# COMO EXECUTAR

Backend

1)  Acesse a pasta do backend cd backend

2)  Execute a aplicação Via Eclipse: Run As → Java Application

Ou via Maven: mvn spring-boot:run

3)  Acesse a API http://localhost:8080

4)  Console H2 http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:prodopt

------------------------------------------------------------------------

# Frontend

1)  Acesse a pasta do frontend cd frontend

2)  Instale as dependências npm install

3)  Execute o servidor npm run dev

4)  Acesse no navegador http://localhost:5173

------------------------------------------------------------------------

# LÓGICA DE OTIMIZAÇÃO

O sistema:

1.  Ordena os produtos por maior preço (ordem decrescente).
2.  Calcula quantas unidades podem ser produzidas com base no estoque
    atual.
3.  Prioriza os produtos mais rentáveis.
4.  Atualiza virtualmente o estoque disponível.
5.  Retorna uma sugestão contendo:
    -   Produto
    -   Quantidade a produzir
    -   Valor total por produto
    -   Valor total geral

------------------------------------------------------------------------

# EXECUÇÃO DOS TESTES

Na pasta backend:

mvn test

Os testes unitários cobrem: - Lógica de planejamento de produção -
Resolução de conflitos entre produtos - Maximização de receita

------------------------------------------------------------------------

# OBJETIVO

Este projeto foi desenvolvido como desafio técnico para demonstrar:

-   Arquitetura organizada
-   Desenvolvimento de API REST
-   Implementação de regra de negócio
-   Integração Frontend + Backend
-   Algoritmo de otimização de produção

------------------------------------------------------------------------

# AUTOR

Carlos Henrique Silva Sousa 
Desenvolvedor Full Stack
