# 📋 Documentação de Endpoints - Personal Finance API

## 🔧 Configuração
- **Base URL**: `http://localhost:8082`
- **Swagger UI**: `http://localhost:8082/swagger-ui/index.html`

---

## 🔐 AuthController - `/auth`

### 1️⃣ POST `/auth/login` - Login de Usuário

**Swagger (Request Body):**
```json
{
  "username": "joao.silva",
  "password": "SenhaForte@123"
}
```

**cURL:**
```bash
curl -X POST http://localhost:8082/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "joao.silva",
    "password": "SenhaForte@123"
  }'
```

---

### 2️⃣ POST `/auth/refresh` - Refresh Token

**Swagger (Query Parameter):**
- refreshToken: `seu_refresh_token_aqui`

**cURL:**
```bash
curl -X POST "http://localhost:8082/auth/refresh?refreshToken=seu_refresh_token_aqui"
```

---

### 3️⃣ POST `/auth/forgot-password` - Solicitar Recuperação de Senha

**Swagger (Request Body):**
```json
{
  "email": "joao.silva@email.com"
}
```

**cURL:**
```bash
curl -X POST http://localhost:8082/auth/forgot-password \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao.silva@email.com"
  }'
```

---

### 4️⃣ POST `/auth/reset-password` - Redefinir Senha com Código

**Swagger (Request Body):**
```json
{
  "email": "joao.silva@email.com",
  "code": "123456",
  "newPassword": "NovaSenha@456"
}
```

**cURL:**
```bash
curl -X POST http://localhost:8082/auth/reset-password \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao.silva@email.com",
    "code": "123456",
    "newPassword": "NovaSenha@456"
  }'
```

---

## 👤 UserController - `/users`

### 5️⃣ POST `/users` - Criar Usuário

**Swagger (Request Body):**
```json
{
  "firstName": "João",
  "lastName": "Silva",
  "email": "joao.silva@email.com",
  "cpf": "123.456.789-09"
}
```

**Exemplo com CPF válido:**
```json
{
  "firstName": "Maria",
  "lastName": "Santos",
  "email": "maria.santos@email.com",
  "cpf": "529.982.247-25"
}
```

**cURL:**
```bash
curl -X POST http://localhost:8082/users \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "João",
    "lastName": "Silva",
    "email": "joao.silva@email.com",
    "cpf": "529.982.247-25"
  }'
```

---

### 6️⃣ PATCH `/users/{id}/password` - Definir Senha Permanente

**Swagger:**
- Path Variable: `id` = `keycloak-user-id-aqui`
- Request Body:
```json
{
  "newPassword": "SenhaForte@123"
}
```

**cURL:**
```bash
curl -X PATCH http://localhost:8082/users/keycloak-user-id-aqui/password \
  -H "Content-Type: application/json" \
  -d '{
    "newPassword": "SenhaForte@123"
  }'
```

---

### 7️⃣ GET `/users` - Buscar Todos os Usuários

**Swagger:**
- Sem parâmetros necessários

**cURL:**
```bash
curl -X GET http://localhost:8082/users
```

---

### 🔒 Endpoints Autenticados (requerem Bearer Token)

> **Nota**: Para os endpoints abaixo, você precisa primeiro fazer login e usar o `access_token` retornado.

**Como adicionar no Swagger:**
1. Clique em "Authorize" no topo da página
2. Cole o token no formato: `Bearer seu_access_token_aqui`

---

### 8️⃣ GET `/users/me` - Buscar Perfil Próprio

**Swagger:**
- Requer autenticação (clique em "Authorize" primeiro)

**cURL:**
```bash
curl -X GET http://localhost:8082/users/me \
  -H "Authorization: Bearer seu_access_token_aqui"
```

---

### 9️⃣ PUT `/users/me` - Atualizar Perfil Próprio

**Swagger (Request Body):**
```json
{
  "email": "novo.email@email.com"
}
```

**cURL:**
```bash
curl -X PUT http://localhost:8082/users/me \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer seu_access_token_aqui" \
  -d '{
    "email": "novo.email@email.com"
  }'
```

---

### 🔟 PATCH `/users/me/password` - Mudar Senha

**Swagger (Request Body):**
```json
{
  "currentPassword": "SenhaAtual@123",
  "newPassword": "NovaSenha@456"
}
```

**cURL:**
```bash
curl -X PATCH http://localhost:8082/users/me/password \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer seu_access_token_aqui" \
  -d '{
    "currentPassword": "SenhaAtual@123",
    "newPassword": "NovaSenha@456"
  }'
```

---

### 1️⃣1️⃣ DELETE `/users/me` - Deletar Conta Própria

**Swagger:**
- Requer autenticação (clique em "Authorize" primeiro)

**cURL:**
```bash
curl -X DELETE http://localhost:8082/users/me \
  -H "Authorization: Bearer seu_access_token_aqui"
```

---

## 🎯 ObjectiveController - `/objectives`

> **Nota**: Todos os endpoints de Objective requerem autenticação (Bearer Token)

### 1️⃣2️⃣ POST `/objectives` - Criar Objetivo Financeiro

**Descrição**: Cria um novo objetivo financeiro ou orçamento para o usuário autenticado.

**Tipos de Objetivo:**
- `LIMITE_CATEGORIA`: Orçamento máximo para uma categoria (Ex: "Gastar no máximo R$ 500 com alimentação")
- `META_ECONOMIA_MES`: Quanto quer economizar no mês (Ex: "Economizar R$ 1.000 em dezembro")
- `META_INVESTIMENTO`: Quanto quer investir no período (Ex: "Investir R$ 500 por mês")

**Swagger (Request Body) - Limite de Categoria:**
```json
{
  "categoriaId": 1,
  "descricao": "Gastar no máximo com alimentação em dezembro",
  "valorObjetivo": 500.00,
  "mesAno": "2024-12",
  "tipo": "LIMITE_CATEGORIA"
}
```

**Swagger (Request Body) - Meta de Economia:**
```json
{
  "descricao": "Economizar R$ 1000 em dezembro",
  "valorObjetivo": 1000.00,
  "mesAno": "2024-12",
  "tipo": "META_ECONOMIA_MES"
}
```

**Swagger (Request Body) - Meta de Investimento:**
```json
{
  "descricao": "Investir R$ 500 por mês",
  "valorObjetivo": 500.00,
  "mesAno": "2024-12",
  "tipo": "META_INVESTIMENTO"
}
```

**cURL:**
```bash
curl -X POST http://localhost:8082/objectives \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer seu_access_token_aqui" \
  -d '{
    "categoriaId": 1,
    "descricao": "Gastar no máximo com alimentação",
    "valorObjetivo": 500.00,
    "mesAno": "2024-12",
    "tipo": "LIMITE_CATEGORIA"
  }'
```

**Response (201 Created):**
```json
{
  "id": 1,
  "categoriaId": 1,
  "categoriaNome": null,
  "descricao": "Gastar no máximo com alimentação",
  "valorObjetivo": 500.00,
  "valorAtual": 0,
  "percentualAtingido": 0.00,
  "saldoRestante": 500.00,
  "mesAno": "2024-12",
  "tipo": "LIMITE_CATEGORIA",
  "ativa": true,
  "statusAlerta": "NENHUM"
}
```

---

### 1️⃣3️⃣ GET `/objectives` - Listar Todos os Objetivos

**Descrição**: Retorna todos os objetivos **ativos** do usuário autenticado.

**Swagger:**
- Requer autenticação (clique em "Authorize" primeiro)

**cURL:**
```bash
curl -X GET http://localhost:8082/objectives \
  -H "Authorization: Bearer seu_access_token_aqui"
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "categoriaId": 1,
    "categoriaNome": null,
    "descricao": "Gastar no máximo com alimentação",
    "valorObjetivo": 500.00,
    "valorAtual": 0,
    "percentualAtingido": 0.00,
    "saldoRestante": 500.00,
    "mesAno": "2024-12",
    "tipo": "LIMITE_CATEGORIA",
    "ativa": true,
    "statusAlerta": "NENHUM"
  },
  {
    "id": 2,
    "categoriaId": null,
    "categoriaNome": null,
    "descricao": "Economizar R$ 1000 em dezembro",
    "valorObjetivo": 1000.00,
    "valorAtual": 850.00,
    "percentualAtingido": 85.00,
    "saldoRestante": 150.00,
    "mesAno": "2024-12",
    "tipo": "META_ECONOMIA_MES",
    "ativa": true,
    "statusAlerta": "AMARELO"
  }
]
```

---

### 1️⃣4️⃣ GET `/objectives/{id}` - Buscar Objetivo por ID

**Descrição**: Retorna um objetivo específico do usuário autenticado.

**Swagger:**
- Path Variable: `id` = `1`
- Requer autenticação

**cURL:**
```bash
curl -X GET http://localhost:8082/objectives/1 \
  -H "Authorization: Bearer seu_access_token_aqui"
```

**Response (200 OK):**
```json
{
  "id": 1,
  "categoriaId": 1,
  "categoriaNome": null,
  "descricao": "Gastar no máximo com alimentação",
  "valorObjetivo": 500.00,
  "valorAtual": 0,
  "percentualAtingido": 0.00,
  "saldoRestante": 500.00,
  "mesAno": "2024-12",
  "tipo": "LIMITE_CATEGORIA",
  "ativa": true,
  "statusAlerta": "NENHUM"
}
```

---

### 1️⃣5️⃣ GET `/objectives/month/{mesAno}` - Buscar Objetivos por Mês

**Descrição**: Retorna todos os objetivos ativos do usuário para um período específico.

**Swagger:**
- Path Variable: `mesAno` = `2024-12` (formato: YYYY-MM)
- Requer autenticação

**cURL:**
```bash
curl -X GET http://localhost:8082/objectives/month/2024-12 \
  -H "Authorization: Bearer seu_access_token_aqui"
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "categoriaId": 1,
    "categoriaNome": null,
    "descricao": "Gastar no máximo com alimentação em dezembro",
    "valorObjetivo": 500.00,
    "valorAtual": 520.00,
    "percentualAtingido": 104.00,
    "saldoRestante": -20.00,
    "mesAno": "2024-12",
    "tipo": "LIMITE_CATEGORIA",
    "ativa": true,
    "statusAlerta": "VERMELHO"
  }
]
```

---

### 1️⃣6️⃣ PATCH `/objectives/{id}/value` - Atualizar Valor do Objetivo

**Descrição**: Atualiza o valor objetivo (meta) de um objetivo existente.

**Swagger:**
- Path Variable: `id` = `1`
- Query Parameter: `novoValor` = `600.00`
- Requer autenticação

**cURL:**
```bash
curl -X PATCH "http://localhost:8082/objectives/1/value?novoValor=600" \
  -H "Authorization: Bearer seu_access_token_aqui"
```

**Response (204 No Content)**

---

### 1️⃣7️⃣ DELETE `/objectives/{id}` - Desativar Objetivo

**Descrição**: Desativa um objetivo (soft delete - não remove do banco, apenas marca como inativo).

**Swagger:**
- Path Variable: `id` = `1`
- Requer autenticação

**cURL:**
```bash
curl -X DELETE http://localhost:8082/objectives/1 \
  -H "Authorization: Bearer seu_access_token_aqui"
```

**Response (204 No Content)**

---

## 🚨 Sistema de Alertas de Objetivos

### Status de Alerta

| Status | Descrição | Quando Ocorre |
|--------|-----------|---------------|
| **NENHUM** | Sem alerta | percentualAtingido < 80% |
| **AMARELO** | Alerta de atenção | 80% ≤ percentualAtingido < 100% |
| **VERMELHO** | Alerta crítico (limite ultrapassado) | percentualAtingido ≥ 100% (apenas para LIMITE_CATEGORIA) |
| **CUMPRIDA** | Meta cumprida com sucesso | percentualAtingido ≥ 100% (apenas para META_ECONOMIA_MES e META_INVESTIMENTO) |

### Cálculos Automáticos

- **valorAtual**: Calculado automaticamente com base nas transações do período
- **percentualAtingido**: `(valorAtual / valorObjetivo) × 100`
- **saldoRestante**: `valorObjetivo - valorAtual` (negativo se ultrapassou)

### Exemplos de Alertas

**Exemplo 1: Limite de Categoria - Alerta AMARELO (80%)**
```json
{
  "descricao": "Gastar no máximo R$ 500 com alimentação",
  "valorObjetivo": 500.00,
  "valorAtual": 400.00,
  "percentualAtingido": 80.00,
  "statusAlerta": "AMARELO"
}
```

**Exemplo 2: Limite de Categoria - Alerta VERMELHO (ultrapassou)**
```json
{
  "descricao": "Gastar no máximo R$ 500 com alimentação",
  "valorObjetivo": 500.00,
  "valorAtual": 520.00,
  "percentualAtingido": 104.00,
  "saldoRestante": -20.00,
  "statusAlerta": "VERMELHO"
}
```

**Exemplo 3: Meta de Economia - CUMPRIDA**
```json
{
  "descricao": "Economizar R$ 1000 em dezembro",
  "valorObjetivo": 1000.00,
  "valorAtual": 1050.00,
  "percentualAtingido": 105.00,
  "statusAlerta": "CUMPRIDA"
}
```

---

## 📝 Regras de Validação

### Senha
- Mínimo 8 caracteres
- Pelo menos uma letra maiúscula
- Pelo menos uma letra minúscula
- Pelo menos um número
- Pelo menos um caractere especial (@$!%*?&)
- **Exemplo válido**: `SenhaForte@123`

### CPF
- Deve ser um CPF válido
- Pode ser com ou sem formatação
- **Exemplos válidos**:
  - `529.982.247-25`
  - `52998224725`

### Email
- Deve ser um email válido
- **Exemplo válido**: `usuario@dominio.com`

### Código de Recuperação
- Exatamente 6 dígitos
- **Exemplo válido**: `123456`

---

## 🔄 Fluxo de Teste Sugerido

### 1. Criar Usuário
```bash
POST /users
{
  "firstName": "Teste",
  "lastName": "Usuario",
  "email": "teste@email.com",
  "cpf": "529.982.247-25"
}
```

### 2. Verificar Email (você receberá uma senha temporária)

### 3. Fazer Login
```bash
POST /auth/login
{
  "username": "teste.usuario",
  "password": "senha_temporaria_do_email"
}
```

### 4. Definir Senha Permanente (opcional)
```bash
PATCH /users/{id}/password
{
  "newPassword": "MinhaSenha@123"
}
```

### 5. Buscar Perfil
```bash
GET /users/me
(Com Bearer Token)
```

### 6. Atualizar Email
```bash
PUT /users/me
{
  "email": "novoemail@email.com"
}
(Com Bearer Token)
```

### 7. Mudar Senha
```bash
PATCH /users/me/password
{
  "currentPassword": "MinhaSenha@123",
  "newPassword": "NovaSenha@456"
}
(Com Bearer Token)
```

### 8. Testar Recuperação de Senha
```bash
# Solicitar código
POST /auth/forgot-password
{
  "email": "teste@email.com"
}

# Redefinir com código (verificar email)
POST /auth/reset-password
{
  "email": "teste@email.com",
  "code": "123456",
  "newPassword": "SenhaRecuperada@789"
}
```

---

## 💡 Dicas para Teste no Swagger

1. **Inicie a aplicação**: Certifique-se de que Keycloak, PostgreSQL e a aplicação estão rodando
2. **Acesse o Swagger**: `http://localhost:8082/swagger-ui/index.html`
3. **Crie um usuário** primeiro usando `POST /users`
4. **Verifique o email** para pegar a senha temporária
5. **Faça login** com `POST /auth/login` para obter o token
6. **Configure a autorização** clicando no botão "Authorize" e colando: `Bearer seu_token_aqui`
7. **Teste os endpoints autenticados** (`/users/me` e derivados)

---

## 🔄 Fluxo de Teste Sugerido - Objectives

### 1. Autenticar
```bash
POST /auth/login
{
  "username": "carlos.garcia",
  "password": "2240"
}
# Salvar o access_token
```

### 2. Criar Limite de Categoria
```bash
POST /objectives
{
  "categoriaId": 1,
  "descricao": "Gastar no máximo R$ 500 com alimentação",
  "valorObjetivo": 500.00,
  "mesAno": "2024-12",
  "tipo": "LIMITE_CATEGORIA"
}
(Com Bearer Token)
```

### 3. Criar Meta de Economia
```bash
POST /objectives
{
  "descricao": "Economizar R$ 1000 em dezembro",
  "valorObjetivo": 1000.00,
  "mesAno": "2024-12",
  "tipo": "META_ECONOMIA_MES"
}
(Com Bearer Token)
```

### 4. Listar Todos os Objetivos
```bash
GET /objectives
(Com Bearer Token)
```

### 5. Buscar Objetivos do Mês
```bash
GET /objectives/month/2024-12
(Com Bearer Token)
```

### 6. Atualizar Valor do Objetivo
```bash
PATCH /objectives/1/value?novoValor=600
(Com Bearer Token)
```

### 7. Desativar Objetivo
```bash
DELETE /objectives/1
(Com Bearer Token)
```

---

## 📊 Resumo dos Endpoints

| Método | Endpoint | Autenticação | Descrição |
|--------|----------|--------------|-----------|
| POST | `/auth/login` | ❌ | Login de usuário |
| POST | `/auth/refresh` | ❌ | Renovar token |
| POST | `/auth/forgot-password` | ❌ | Solicitar recuperação de senha |
| POST | `/auth/reset-password` | ❌ | Redefinir senha com código |
| POST | `/users` | ❌ | Criar novo usuário |
| PATCH | `/users/{id}/password` | ❌ | Definir senha permanente |
| GET | `/users` | ❌ | Buscar todos os usuários |
| GET | `/users/me` | ✅ | Buscar perfil próprio |
| PUT | `/users/me` | ✅ | Atualizar perfil próprio |
| PATCH | `/users/me/password` | ✅ | Mudar senha |
| DELETE | `/users/me` | ✅ | Deletar conta própria |
| POST | `/objectives` | ✅ | Criar objetivo financeiro |
| GET | `/objectives` | ✅ | Listar todos os objetivos ativos |
| GET | `/objectives/{id}` | ✅ | Buscar objetivo por ID |
| GET | `/objectives/month/{mesAno}` | ✅ | Buscar objetivos por mês |
| PATCH | `/objectives/{id}/value` | ✅ | Atualizar valor do objetivo |
| DELETE | `/objectives/{id}` | ✅ | Desativar objetivo |

---

## 🎯 CPFs Válidos para Teste

Use estes CPFs válidos nos seus testes:
- `529.982.247-25`
- `111.444.777-35`
- `123.456.789-09`
- `000.000.001-91`
