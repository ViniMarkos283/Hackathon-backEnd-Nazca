# 🏆 Hackathon - Backend Nazca

> Projeto desenvolvido em dupla durante um Hackathon, no qual a equipe foi classificada em segundo lugar. Fui responsável pela camada back-end: arquitetura, banco de dados e deploy.

---

## 📌 Sobre o Projeto

O **Nazca** é uma API REST desenvolvida para solucionar um desafio de **gestão de POPs (Procedimentos Operacionais Padrão)** em ambiente corporativo.

O sistema permite que empresas gerenciem seus procedimentos internos, controlem o status de treinamentos dos colaboradores por cargo e setor, e recebam alertas automáticos sobre vencimentos e pendências — tudo com dados consolidados em um dashboard.

---

## 🗂️ Repositórios do Projeto

| Camada | Repositório | Responsável |
|--------|-------------|-------------|
| Back-end | [Hackathon-backEnd-Nazca](https://github.com/ViniMarkos283/Hackathon-backEnd-Nazca) | [Marcos Vinícius](https://github.com/ViniMarkos283) · [Pedro Luiz](https://github.com/pedroluizjanuario-alt) |
| Front-end | [sistemanazca](https://github.com/cayoananias/sistemanazca) | Equipe |

### 🖥️ Sobre o Front-end

Interface web desenvolvida com **HTML, CSS e JavaScript puros**, consumindo diretamente os endpoints da API REST. Apresenta o dashboard com os indicadores do sistema, listagem de POPs, colaboradores, treinamentos e alertas — sem dependência de frameworks externos.

---

## ⚙️ Funcionalidades

- 📋 **Gestão de POPs** — cadastro, versionamento, controle de validade e status (ativo, em revisão, obsoleto)
- 👥 **Gestão de Colaboradores** — vinculação por cargo e setor, com rastreamento de treinamentos obrigatórios
- 📊 **Conformidade** — cálculo automático do percentual de conformidade por colaborador e por setor
- 🔔 **Alertas automáticos** — geração de alertas para POPs vencidos, treinamentos atrasados e revisões pendentes (via job agendado diário)
- 📁 **Importação via Excel** — upload de planilha `.xlsx` para cadastro em lote de POPs
- 🗂️ **Evidências de treinamento** — registro e consulta de evidências por colaborador
- 📈 **Dashboard** — resumo geral com totais de POPs, colaboradores ativos, treinamentos do mês, alertas não lidos e vencimentos próximos
- 📖 **Documentação interativa** — Swagger UI disponível em `/swagger-ui.html`

---

## 🛠️ Tecnologias Utilizadas

| Camada | Tecnologia |
|--------|------------|
| Linguagem | Java 21 |
| Framework | Spring Boot 3.3 |
| Banco de Dados | MySQL |
| Migrations | Flyway |
| ORM | Spring Data JPA / Hibernate |
| Documentação | SpringDoc OpenAPI (Swagger) |
| Importação Excel | Apache POI |
| Containerização | Docker |
| Build | Maven |
| Utilitários | Lombok |

---

## 🏗️ Arquitetura

O projeto segue o padrão **MVC em camadas**, organizado por responsabilidade:

```
com.nazca/
├── controller/   → Endpoints REST
├── service/      → Regras de negócio e jobs agendados
├── repository/   → Acesso ao banco de dados (Spring Data JPA)
├── model/        → Entidades e enums
├── dto/          → Objetos de requisição e resposta
├── exception/    → Tratamento global de erros
└── config/       → Configurações (OpenAPI, etc.)
```

---

## 🔗 Principais Endpoints

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/dashboard` | Resumo geral do sistema |
| `GET` | `/api/pops` | Lista todos os POPs com status label |
| `POST` | `/api/pops/importar` | Importa POPs via planilha Excel |
| `GET` | `/api/pops/vencimentos-criticos` | POPs vencendo nos próximos 30 dias |
| `GET` | `/api/colaboradores/conformidade` | Conformidade individual por colaborador |
| `GET` | `/api/treinamentos/conformidade-setor` | Conformidade agrupada por setor |
| `GET` | `/api/alertas` | Lista alertas (filtro: apenas não lidos) |
| `PUT` | `/api/alertas/{id}/lido` | Marca alerta como lido |

> Documentação completa disponível via Swagger após subir a aplicação.

---

## 🚀 Como Rodar

### Pré-requisitos
- Docker e Docker Compose instalados
- Java 21+ (caso queira rodar sem Docker)

### Com Docker

```bash
# Clone o repositório
git clone https://github.com/ViniMarkos283/Hackathon-backEnd-Nazca.git
cd Hackathon-backEnd-Nazca

# Suba a aplicação
docker build -t nazca-api .
docker run -p 8080:8080 nazca-api
```

### Sem Docker

```bash
# Configure o banco no application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/nazca_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

# Rode a aplicação
./mvnw spring-boot:run
```

Acesse a documentação em: `http://localhost:8080/swagger-ui.html`

---

## 👥 Equipe

| Papel | Desenvolvedor |
|-------|--------------|
| Back-end · Banco de Dados · Deploy | [Marcos Vinícius](https://github.com/ViniMarkos283) |
| Back-end | [Pedro Luiz](https://github.com/pedroluizjanuario-alt) |
| Front-end | [Cayo Ananias](https://github.com/cayoananias) |
