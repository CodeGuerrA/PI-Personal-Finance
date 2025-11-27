# 💰 Personal Finance - Sistema de Gerenciamento Financeiro Pessoal

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.8-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![Keycloak](https://img.shields.io/badge/Keycloak-24.0.5-red.svg)](https://www.keycloak.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 Índice

- [Visão Geral](#-visão-geral)
- [Arquitetura](#-arquitetura)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Funcionalidades](#-funcionalidades)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Configuração](#-instalação-e-configuração)
- [Executando a Aplicação](#-executando-a-aplicação)
- [Documentação da API](#-documentação-da-api)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Segurança](#-segurança)
- [Banco de Dados](#-banco-de-dados)
- [Testes](#-testes)
- [Resolução de Problemas](#-resolução-de-problemas)
- [Contribuindo](#-contribuindo)
- [Licença](#-licença)

---

## 🎯 Visão Geral

O **Personal Finance** é uma aplicação enterprise de gerenciamento financeiro pessoal desenvolvida com Spring Boot 3, seguindo os princípios de **Clean Architecture** e **Hexagonal Architecture (Ports & Adapters)**. O sistema oferece autenticação e autorização robustas através do Keycloak, garantindo segurança de nível corporativo.

### 🎨 Principais Diferenciais

- **Arquitetura Limpa**: Separação clara de responsabilidades entre camadas (API, Application, Domain, Infrastructure)
- **Segurança Enterprise**: Integração completa com Keycloak para autenticação OAuth2 e JWT
- **Validações em Múltiplas Camadas**: Validações de formato (Bean Validation), domínio e negócio
- **Gestão de Transações**: Rollback automático com estratégias de compensação
- **Entidades Ricas**: Domain-Driven Design (DDD) com validações intrínsecas
- **Ports & Adapters**: Desacoplamento total de frameworks e tecnologias
- **Código Limpo**: Seguindo princípios SOLID e Clean Code

### 🏗️ O Que o Sistema Faz?

Este sistema permite que usuários:

1. **Criem contas** com validação completa de dados (CPF, email, etc.)
2. **Façam login seguro** com tokens JWT gerenciados pelo Keycloak
3. **Gerenciem seus perfis** com atualização de informações pessoais
4. **Recuperem senhas** através de códigos enviados por email
5. **Alterem senhas** com validação da senha atual
6. **Excluam suas contas** com remoção completa dos dados

O sistema está preparado para expansão com módulos de:
- Gerenciamento de transações financeiras
- Categorização de despesas e receitas
- Relatórios e dashboards financeiros
- Metas de economia
- Controle de orçamentos

---

## 🏛️ Arquitetura

O projeto segue uma arquitetura em camadas, combinando **Clean Architecture** e **Hexagonal Architecture**:

```
┌─────────────────────────────────────────────────────────────┐
│                       API LAYER                              │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Controllers: Recebem requisições HTTP              │   │
│  │  DTOs: Objetos de transferência de dados            │   │
│  │  Mappers: Conversão entre DTOs e Entities           │   │
│  └──────────────────────────────────────────────────────┘   │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────────┐
│                   APPLICATION LAYER                          │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Facades: Interface simplificada para o Controller  │   │
│  │  Services: Lógica de aplicação (CRUD)               │   │
│  │  Orchestrators: Coordenação de operações complexas  │   │
│  └──────────────────────────────────────────────────────┘   │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────────┐
│                     DOMAIN LAYER                             │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Entities: Entidades ricas com validações           │   │
│  │  Ports: Interfaces (contratos)                       │   │
│  │  Domain Services: Regras de negócio complexas       │   │
│  │  Exceptions: Exceções específicas do domínio        │   │
│  └──────────────────────────────────────────────────────┘   │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────────┐
│                 INFRASTRUCTURE LAYER                         │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Adapters: Implementação dos Ports                  │   │
│  │  Repositories: Acesso ao banco de dados (JPA)       │   │
│  │  External Services: Keycloak, Email                 │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### 📐 Princípios Arquiteturais

#### 1. **Separação de Responsabilidades**
Cada camada tem uma responsabilidade única e bem definida:

- **API Layer**: Entrada/Saída HTTP - não contém lógica de negócio
- **Application Layer**: Orquestração de casos de uso
- **Domain Layer**: Regras de negócio e validações - núcleo da aplicação
- **Infrastructure Layer**: Detalhes técnicos (banco de dados, APIs externas)

#### 2. **Inversão de Dependência**
```
Application Layer → depende de → Ports (Interfaces)
                                      ↑
Infrastructure Layer → implementa → Ports
```

O domínio **não conhece** a infraestrutura. Apenas define contratos (Ports) que a infraestrutura implementa.

#### 3. **Domain-Driven Design (DDD)**
- **Entidades Ricas**: Validações e comportamentos dentro das entidades
- **Value Objects**: Objetos imutáveis (CPF, Email)
- **Aggregates**: Grupos de entidades tratados como unidade
- **Domain Services**: Lógica que não pertence a uma entidade específica

---

## 🛠️ Tecnologias Utilizadas

### Core Framework
- **Java 21** - Última versão LTS com records, pattern matching e muito mais
- **Spring Boot 3.5.8** - Framework principal para aplicações enterprise
- **Maven** - Gerenciamento de dependências e build

### Segurança
- **Keycloak 24.0.5** - Identity and Access Management (IAM)
- **Spring Security** - Framework de segurança do Spring
- **OAuth2 Resource Server** - Validação de tokens JWT
- **JWT (Auth0)** - Biblioteca para manipulação de tokens JWT

### Banco de Dados
- **PostgreSQL** - Banco de dados relacional principal
- **H2 Database** - Banco de dados em memória para testes
- **Spring Data JPA** - Abstração para acesso a dados
- **Hibernate** - ORM (Object-Relational Mapping)

### Validação e Mapeamento
- **Bean Validation (Jakarta)** - Validações declarativas
- **MapStruct 1.6.3** - Mapeamento de objetos (DTO ↔ Entity)
- **Lombok** - Redução de código boilerplate

### Email
- **Spring Mail** - Envio de emails transacionais
- **SMTP Gmail** - Servidor de email

### Documentação
- **SpringDoc OpenAPI 3** - Documentação automática da API (Swagger)

### Testes
- **JUnit 5** - Framework de testes unitários
- **Spring Boot Test** - Suporte para testes de integração
- **Spring Security Test** - Testes de segurança

---

## ✨ Funcionalidades

### 🔐 Autenticação e Autorização

#### 1. **Registro de Usuário** (`POST /users`)
**Fluxo Completo:**

```
1. Cliente envia dados → Controller recebe e valida formato
                             ↓
2. Service valida regras → Verifica se email/CPF já existe
                             ↓
3. Cria usuário no Keycloak → Gera senha temporária
                             ↓
4. Salva usuário no banco → Transaction com rollback automático
                             ↓
5. Envia email de boas-vindas → Senha temporária para o usuário
                             ↓
6. Retorna sucesso (201 Created)
```

**Validações Realizadas:**
- ✅ Email no formato válido
- ✅ CPF válido (algoritmo de validação completo)
- ✅ Username único no sistema
- ✅ Email não cadastrado anteriormente
- ✅ CPF não cadastrado anteriormente

**Exemplo de Requisição:**
```json
POST /users
Content-Type: application/json

{
  "userName": "joao.silva",
  "firstName": "João",
  "lastName": "Silva",
  "email": "joao.silva@example.com",
  "cpf": "12345678901"
}
```

**Rollback Automático:**
Se qualquer etapa falhar após a criação no Keycloak, o sistema automaticamente remove o usuário do Keycloak (estratégia de compensação).

#### 2. **Login** (`POST /auth/login`)
**Fluxo Completo:**

```
1. Cliente envia credenciais → Controller recebe
                                   ↓
2. Service envia para Keycloak → Keycloak valida
                                   ↓
3. Keycloak retorna tokens → Access Token + Refresh Token
                                   ↓
4. Sistema busca dados do usuário → Complementa informações
                                   ↓
5. Retorna resposta completa → Tokens + dados do usuário
```

**Exemplo de Requisição:**
```json
POST /auth/login
Content-Type: application/json

{
  "username": "joao.silva",
  "password": "senha123"
}
```

**Exemplo de Resposta:**
```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCI...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI...",
  "expiresIn": 300,
  "refreshExpiresIn": 1800,
  "tokenType": "Bearer",
  "user": {
    "id": 1,
    "userName": "joao.silva",
    "firstName": "João",
    "lastName": "Silva",
    "email": "joao.silva@example.com"
  }
}
```

#### 3. **Refresh Token** (`POST /auth/refresh`)
**Por que Refresh Token é importante?**

- **Access Token**: Válido por 5 minutos (curto por segurança)
- **Refresh Token**: Válido por 30 minutos

Quando o Access Token expira, o cliente usa o Refresh Token para obter um novo Access Token **sem pedir senha novamente**.

**Exemplo de Requisição:**
```json
POST /auth/refresh
Content-Type: application/json

"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### 4. **Recuperação de Senha** (`POST /auth/forgot-password`)
**Fluxo Completo:**

```
1. Usuário informa email → Sistema valida se existe
                              ↓
2. Gera código de 6 dígitos → Armazena no banco com TTL de 15 minutos
                              ↓
3. Envia email → Código de recuperação
                              ↓
4. Usuário recebe email → Usa código para redefinir senha
```

**Exemplo de Requisição:**
```json
POST /auth/forgot-password
Content-Type: application/json

{
  "email": "joao.silva@example.com"
}
```

#### 5. **Redefinir Senha com Código** (`POST /auth/reset-password`)
**Exemplo de Requisição:**
```json
POST /auth/reset-password
Content-Type: application/json

{
  "email": "joao.silva@example.com",
  "code": "123456",
  "newPassword": "novaSenha@2024"
}
```

**Validações:**
- ✅ Código válido e não expirado
- ✅ Email corresponde ao código
- ✅ Senha atende requisitos de segurança

### 👤 Gerenciamento de Perfil

#### 6. **Ver Meu Perfil** (`GET /users/me`)
**Autenticação Necessária**: ✅ Sim

**Fluxo:**
```
1. Cliente envia token JWT → Spring Security valida
                                ↓
2. Extrai Keycloak ID do token → AuthenticatedUserProvider
                                ↓
3. Busca usuário no banco → Por Keycloak ID
                                ↓
4. Retorna dados (sem informações sensíveis)
```

**Exemplo de Requisição:**
```http
GET /users/me
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI...
```

**Exemplo de Resposta:**
```json
{
  "id": 1,
  "userName": "joao.silva",
  "firstName": "João",
  "lastName": "Silva",
  "email": "joao.silva@example.com",
  "createdAt": "2024-01-15T10:30:00"
}
```

**Segurança:**
- ❌ Não retorna: senha, keycloakId, CPF (informações sensíveis)
- ✅ Usuário **só vê seus próprios dados** (impossível ver dados de outros)

#### 7. **Atualizar Meu Perfil** (`PUT /users/me`)
**Exemplo de Requisição:**
```http
PUT /users/me
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI...
Content-Type: application/json

{
  "firstName": "João Pedro",
  "lastName": "Silva Santos",
  "email": "joao.pedro@example.com"
}
```

**Validações:**
- ✅ Se email for alterado, verifica se já existe outro usuário com esse email
- ✅ Atualiza no banco **E** no Keycloak (sincronização)
- ✅ Email válido

#### 8. **Alterar Minha Senha** (`PATCH /users/me/password`)
**Exemplo de Requisição:**
```http
PATCH /users/me/password
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI...
Content-Type: application/json

{
  "currentPassword": "senhaAntiga123",
  "newPassword": "novaSenha@2024"
}
```

**Validações:**
- ✅ Senha atual está correta
- ✅ Nova senha diferente da atual
- ✅ Nova senha atende requisitos de segurança

**Fluxo:**
```
1. Valida senha atual → Autentica no Keycloak
                           ↓
2. Se válida → Atualiza senha no Keycloak
                           ↓
3. Invalida sessões antigas → Força novo login
```

#### 9. **Deletar Minha Conta** (`DELETE /users/me`)
**Exemplo de Requisição:**
```http
DELETE /users/me
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI...
```

**Fluxo de Deleção:**
```
1. Identifica usuário → Pelo token JWT
                          ↓
2. Deleta do banco → Cascade em todas as tabelas relacionadas
                          ↓
3. Deleta do Keycloak → Remove usuário e sessões
                          ↓
4. Retorna 204 No Content
```

### 👥 Administração (Endpoints Futuros)

- Listar todos os usuários
- Buscar usuário por ID
- Ativar/desativar usuários
- Atribuir roles e permissões

---

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

### Obrigatórios

| Software | Versão Mínima | Propósito |
|----------|---------------|-----------|
| **Java JDK** | 21 | Linguagem de programação |
| **Maven** | 3.8+ | Gerenciamento de dependências |
| **PostgreSQL** | 13+ | Banco de dados principal |
| **Keycloak** | 24.0+ | Servidor de autenticação |

### Opcionais

| Software | Propósito |
|----------|-----------|
| **Docker** | Para rodar Keycloak e PostgreSQL em containers |
| **Postman/Insomnia** | Para testar a API |
| **IntelliJ IDEA** | IDE recomendada |

---

## 🚀 Instalação e Configuração

### Passo 1: Clonar o Repositório

```bash
git clone https://github.com/seu-usuario/personal-finance.git
cd personal-finance
```

### Passo 2: Configurar o PostgreSQL

#### Opção A: Usando Docker (Recomendado)

```bash
docker run --name personal-finance-db \
  -e POSTGRES_DB=personal_finance_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=224046 \
  -p 5432:5432 \
  -d postgres:15
```

#### Opção B: Instalação Local

1. Instale o PostgreSQL: https://www.postgresql.org/download/
2. Crie o banco de dados:

```sql
CREATE DATABASE personal_finance_db;
CREATE USER postgres WITH PASSWORD '224046';
GRANT ALL PRIVILEGES ON DATABASE personal_finance_db TO postgres;
```

**Verificar se está funcionando:**
```bash
psql -U postgres -d personal_finance_db
```

### Passo 3: Configurar o Keycloak

#### Opção A: Usando Docker (Recomendado)

```bash
docker run --name personal-finance-keycloak \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  -p 8081:8080 \
  quay.io/keycloak/keycloak:24.0.5 start-dev
```

#### Opção B: Download Standalone

1. Baixe o Keycloak: https://www.keycloak.org/downloads
2. Extraia e execute:

```bash
cd keycloak-24.0.5
bin/kc.sh start-dev --http-port=8081
```

#### Configuração do Keycloak (IMPORTANTE!)

Após iniciar o Keycloak, acesse: http://localhost:8081

**1. Login como Admin:**
- Username: `admin`
- Password: `admin`

**2. Criar Realm:**
```
1. Clique em "Master" (dropdown no canto superior esquerdo)
2. Clique em "Create Realm"
3. Name: personal-finance-realm
4. Clique em "Create"
```

**3. Criar Client:**
```
1. No menu lateral: Clients → Create client
2. Client ID: personal-finance
3. Client authentication: ON
4. Valid redirect URIs: http://localhost:8082/*
5. Web origins: http://localhost:8082
6. Salve
```

**4. Obter Client Secret:**
```
1. Vá em Clients → personal-finance
2. Aba "Credentials"
3. Copie o "Client Secret"
4. Cole no application.properties (keycloak.client-secret)
```

**5. Configurar Token Lifespans:**
```
1. Realm Settings → Tokens
2. Access Token Lifespan: 5 minutes
3. Refresh Token Lifespan: 30 minutes
4. Salve
```

### Passo 4: Configurar Email (Gmail)

Para enviar emails, você precisa de uma senha de aplicativo do Gmail:

**1. Ativar verificação em duas etapas:**
- Acesse: https://myaccount.google.com/security
- Ative a verificação em duas etapas

**2. Gerar senha de aplicativo:**
- Acesse: https://myaccount.google.com/apppasswords
- Selecione "Email" e "Outro (nome personalizado)"
- Digite: "Personal Finance App"
- Copie a senha gerada (16 caracteres)

**3. Configurar no application.properties:**
```properties
spring.mail.username=seu-email@gmail.com
spring.mail.password=senha-de-aplicativo-gerada
```

### Passo 5: Configurar application.properties

Edite o arquivo: `src/main/resources/application.properties`

```properties
# Servidor
spring.application.name=personal-finance
server.port=8082

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/personal_finance_db
spring.datasource.username=postgres
spring.datasource.password=224046
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Keycloak
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8081/realms/personal-finance-realm
keycloak.realm=personal-finance-realm
keycloak.client-id=personal-finance
keycloak.client-secret=SEU_CLIENT_SECRET_AQUI
keycloak.server-url=http://localhost:8081
keycloak.admin-username=admin
keycloak.admin-password=admin

# Email
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=seu-email@gmail.com
spring.mail.password=sua-senha-de-aplicativo
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### Passo 6: Instalar Dependências

```bash
./mvnw clean install
```

Este comando:
- ✅ Baixa todas as dependências do Maven
- ✅ Compila o projeto
- ✅ Executa os testes
- ✅ Gera o arquivo JAR

---

## ▶️ Executando a Aplicação

### Modo Desenvolvimento

```bash
./mvnw spring-boot:run
```

### Modo Produção

```bash
# Compilar
./mvnw clean package -DskipTests

# Executar JAR
java -jar target/personal-finance-0.0.1-SNAPSHOT.jar
```

### Verificar se está funcionando

Acesse: http://localhost:8082

**Você deve ver:**
```json
{
  "timestamp": "2024-01-15T10:30:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource"
}
```

✅ Se você vir esta mensagem, está funcionando! (401 é esperado, pois não enviamos token)

**Endpoints públicos para testar:**
- http://localhost:8082/swagger-ui/index.html (Documentação Swagger)
- http://localhost:8082/v3/api-docs (OpenAPI JSON)

---

## 📚 Documentação da API

### Swagger UI

Acesse a documentação interativa: **http://localhost:8082/swagger-ui/index.html**

O Swagger permite:
- ✅ Ver todos os endpoints disponíveis
- ✅ Ver modelos de requisição/resposta
- ✅ Testar endpoints diretamente pelo navegador
- ✅ Gerar código cliente em várias linguagens

### Endpoints Disponíveis

#### 🔐 Autenticação (`/auth`)

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/auth/login` | Fazer login | ❌ Não |
| POST | `/auth/refresh` | Renovar token | ❌ Não |
| POST | `/auth/forgot-password` | Solicitar recuperação de senha | ❌ Não |
| POST | `/auth/reset-password` | Redefinir senha com código | ❌ Não |

#### 👤 Usuários (`/users`)

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/users` | Criar novo usuário | ❌ Não |
| GET | `/users` | Listar todos os usuários | ❌ Não |
| GET | `/users/me` | Ver meu perfil | ✅ Sim |
| PUT | `/users/me` | Atualizar meu perfil | ✅ Sim |
| PATCH | `/users/me/password` | Alterar minha senha | ✅ Sim |
| DELETE | `/users/me` | Deletar minha conta | ✅ Sim |
| PATCH | `/users/{id}/password` | Definir senha (primeira vez) | ❌ Não |

### Exemplos de Uso com cURL

#### 1. Criar Usuário

```bash
curl -X POST http://localhost:8082/users \
  -H "Content-Type: application/json" \
  -d '{
    "userName": "maria.oliveira",
    "firstName": "Maria",
    "lastName": "Oliveira",
    "email": "maria.oliveira@example.com",
    "cpf": "12345678901"
  }'
```

#### 2. Login

```bash
curl -X POST http://localhost:8082/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "maria.oliveira",
    "password": "senha-temporaria-do-email"
  }'
```

**Salve o accessToken da resposta!**

#### 3. Ver Meu Perfil

```bash
curl -X GET http://localhost:8082/users/me \
  -H "Authorization: Bearer SEU_ACCESS_TOKEN_AQUI"
```

#### 4. Atualizar Perfil

```bash
curl -X PUT http://localhost:8082/users/me \
  -H "Authorization: Bearer SEU_ACCESS_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Maria Clara",
    "lastName": "Oliveira Santos",
    "email": "maria.clara@example.com"
  }'
```

#### 5. Alterar Senha

```bash
curl -X PATCH http://localhost:8082/users/me/password \
  -H "Authorization: Bearer SEU_ACCESS_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{
    "currentPassword": "senhaAtual123",
    "newPassword": "novaSenha@2024"
  }'
```

---

## 📁 Estrutura do Projeto

```
personal-finance/
├── src/
│   ├── main/
│   │   ├── java/com/personalFinance/personal_finance/
│   │   │   ├── PersonalFinanceApplication.java
│   │   │   │
│   │   │   ├── shared/                    # Módulo Compartilhado
│   │   │   │   ├── config/
│   │   │   │   │   ├── SecurityConfig.java
│   │   │   │   │   ├── KeycloakConfig.java
│   │   │   │   │   └── converter/
│   │   │   │   │       └── JwtAuthConverter.java
│   │   │   │   ├── security/
│   │   │   │   │   └── AuthenticatedUserProvider.java
│   │   │   │   ├── exception/
│   │   │   │   │   └── GlobalExceptionHandler.java
│   │   │   │   └── validator/
│   │   │   │       ├── Validator.java
│   │   │   │       ├── BiValidator.java
│   │   │   │       └── ValidationResult.java
│   │   │   │
│   │   │   └── user/                      # Módulo de Usuário
│   │   │       ├── api/                   # Camada de API
│   │   │       │   ├── controller/
│   │   │       │   │   ├── UserController.java
│   │   │       │   │   └── AuthController.java
│   │   │       │   ├── dto/
│   │   │       │   │   ├── request/
│   │   │       │   │   │   ├── UserCreateRequestDTO.java
│   │   │       │   │   │   ├── UserUpdateRequestDTO.java
│   │   │       │   │   │   ├── UserLoginRequestDTO.java
│   │   │       │   │   │   ├── ChangePasswordRequestDTO.java
│   │   │       │   │   │   ├── ForgotPasswordRequestDTO.java
│   │   │       │   │   │   ├── ResetPasswordWithCodeRequestDTO.java
│   │   │       │   │   │   └── UserSetPasswordRequestDTO.java
│   │   │       │   │   └── response/
│   │   │       │   │       ├── UserResponseDTO.java
│   │   │       │   │       ├── UserLoginResponseDTO.java
│   │   │       │   │       └── KeycloakUserResponseDTO.java
│   │   │       │   └── mapper/
│   │   │       │       ├── UserMapper.java
│   │   │       │       └── AuthResponseMapper.java
│   │   │       │
│   │   │       ├── application/           # Camada de Aplicação
│   │   │       │   ├── facade/
│   │   │       │   │   ├── UserService.java (interface)
│   │   │       │   │   ├── UserServiceFacade.java
│   │   │       │   │   ├── AuthService.java (interface)
│   │   │       │   │   └── AuthServiceFacade.java
│   │   │       │   ├── service/
│   │   │       │   │   ├── UserCreator.java
│   │   │       │   │   ├── UserFinder.java
│   │   │       │   │   ├── UserUpdater.java
│   │   │       │   │   ├── UserDeleter.java
│   │   │       │   │   ├── PasswordRecoveryService.java
│   │   │       │   │   └── PasswordRecoveryCodeManager.java
│   │   │       │   ├── orchestrator/
│   │   │       │   │   ├── SaveUserOrchestrator.java
│   │   │       │   │   ├── UpdateUserOrchestrator.java
│   │   │       │   │   ├── DeleteUserOrchestrator.java
│   │   │       │   │   ├── RollbackStrategy.java
│   │   │       │   │   └── KeycloakRollbackStrategy.java
│   │   │       │   └── auth/
│   │   │       │       ├── UserAuth.java
│   │   │       │       ├── UserRefreshToken.java
│   │   │       │       └── UserPasswordManager.java
│   │   │       │
│   │   │       ├── domain/                # Camada de Domínio
│   │   │       │   ├── entity/
│   │   │       │   │   └── User.java
│   │   │       │   ├── port/
│   │   │       │   │   ├── UserSavePort.java
│   │   │       │   │   ├── UserFindPort.java
│   │   │       │   │   ├── UserUpdatePort.java
│   │   │       │   │   ├── UserDeletePort.java
│   │   │       │   │   └── UserExistencePort.java
│   │   │       │   ├── service/
│   │   │       │   │   └── validation/
│   │   │       │   │       ├── EmailValidator.java
│   │   │       │   │       ├── EmailUpdateValidator.java
│   │   │       │   │       ├── EmailNormalizer.java
│   │   │       │   │       ├── CPFValidator.java
│   │   │       │   │       ├── CpfNormalizer.java
│   │   │       │   │       └── UsernameGenerator.java
│   │   │       │   └── exception/
│   │   │       │       ├── UserNotFoundException.java
│   │   │       │       ├── InvalidEmailException.java
│   │   │       │       ├── InvalidCpfException.java
│   │   │       │       ├── DuplicateEmailException.java
│   │   │       │       ├── DuplicateUsernameException.java
│   │   │       │       ├── DuplicateCpfException.java
│   │   │       │       ├── UnauthorizedAccessException.java
│   │   │       │       └── UserPersistenceException.java
│   │   │       │
│   │   │       └── infrastructure/        # Camada de Infraestrutura
│   │   │           ├── repository/
│   │   │           │   ├── UserRepository.java
│   │   │           │   └── adapter/
│   │   │           │       ├── UserPersistenceAdapter.java
│   │   │           │       ├── UserQueryAdapter.java
│   │   │           │       └── UserExistenceAdapter.java
│   │   │           └── external/
│   │   │               ├── keycloak/
│   │   │               │   ├── config/
│   │   │               │   │   ├── KeycloakPropertiesAdmin.java
│   │   │               │   │   └── KeycloakPropertiesClient.java
│   │   │               │   ├── admin/
│   │   │               │   │   ├── KeycloakUserCreator.java
│   │   │               │   │   ├── KeycloakUserUpdater.java
│   │   │               │   │   ├── KeycloakUserDeleter.java
│   │   │               │   │   ├── KeycloakPasswordManager.java
│   │   │               │   │   └── KeycloakUserChecker.java
│   │   │               │   ├── auth/
│   │   │               │   │   ├── KeycloakAuthenticator.java
│   │   │               │   │   └── KeycloakTokenRefresher.java
│   │   │               │   └── facade/
│   │   │               │       ├── KeycloakFacade.java (interface)
│   │   │               │       └── KeycloakFacadeImpl.java
│   │   │               └── email/
│   │   │                   ├── EmailSenderService.java
│   │   │                   ├── WelcomeEmailService.java
│   │   │                   ├── WelcomeEmailContentBuilder.java
│   │   │                   ├── UserNotificationEmailService.java
│   │   │                   └── PasswordRecoveryEmailService.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-test.properties
│   │
│   └── test/
│       └── java/com/personalFinance/personal_finance/
│           ├── PersonalFinanceApplicationTests.java
│           └── user/
│               ├── api/
│               │   └── mapper/
│               │       └── UserMapperTest.java
│               ├── application/
│               │   ├── auth/
│               │   │   └── UserPasswordManagerTest.java
│               │   └── service/
│               │       └── UserCreatorTest.java
│               ├── domain/
│               │   └── service/
│               │       └── validation/
│               │           └── UsernameGeneratorTest.java
│               └── infrastructure/
│                   └── external/
│                       └── email/
│                           └── UserNotificationEmailServiceTest.java
│
├── .gitignore
├── .gitattributes
├── pom.xml
├── mvnw
├── mvnw.cmd
├── README.md
└── README-MODULOS-USER-SHARED.md
```

### 📂 Explicação das Camadas

#### **API Layer** (`api/`)
- **Controllers**: Recebem requisições HTTP, validam formato, chamam services
- **DTOs**: Objetos para transferência de dados (Request/Response)
- **Mappers**: Convertem DTOs ↔ Entities

**Responsabilidade**: Comunicação HTTP - entrada/saída

#### **Application Layer** (`application/`)
- **Facades**: Interface simplificada para os controllers
- **Services**: Lógica de aplicação (UserCreator, UserFinder, etc.)
- **Orchestrators**: Coordenam operações complexas com rollback
- **Auth**: Serviços de autenticação e gerenciamento de senhas

**Responsabilidade**: Orquestração de casos de uso

#### **Domain Layer** (`domain/`)
- **Entities**: Entidades ricas com validações
- **Ports**: Interfaces (contratos) que a infraestrutura implementa
- **Domain Services**: Validações e regras de negócio
- **Exceptions**: Exceções específicas do domínio

**Responsabilidade**: Regras de negócio - núcleo da aplicação

#### **Infrastructure Layer** (`infrastructure/`)
- **Repositories**: Acesso ao banco de dados (JPA)
- **Adapters**: Implementações dos Ports
- **External Services**: Integrações (Keycloak, Email)

**Responsabilidade**: Detalhes técnicos e integrações externas

---

## 🔒 Segurança

### Autenticação JWT

O sistema usa **OAuth2 + JWT** para autenticação:

1. **Cliente faz login** → Envia username/password
2. **Keycloak valida** → Gera Access Token (JWT) + Refresh Token
3. **Cliente usa token** → Envia em todas as requisições
4. **Spring Security valida** → Verifica assinatura e expiração

### Estrutura do JWT

```json
{
  "header": {
    "alg": "RS256",
    "typ": "JWT"
  },
  "payload": {
    "sub": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "preferred_username": "joao.silva",
    "email": "joao.silva@example.com",
    "realm_access": {
      "roles": ["user"]
    },
    "exp": 1642248000,
    "iat": 1642247700
  },
  "signature": "..."
}
```

### Como Enviar o Token

Todas as requisições autenticadas devem incluir o header:

```http
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Endpoints Públicos vs Protegidos

| Tipo | Endpoints | Autenticação |
|------|-----------|--------------|
| **Públicos** | `/swagger-ui/**`, `/v3/api-docs/**`, `/auth/**`, `POST /users`, `GET /users`, `PATCH /users/*/password` | ❌ Não |
| **Protegidos** | `/users/me/**` | ✅ Sim |

### CORS (Cross-Origin Resource Sharing)

O sistema permite requisições de:
- `http://localhost:3000` (React/Next.js)
- `http://127.0.0.1:5500` (Live Server)

**Métodos permitidos**: GET, POST, PUT, DELETE, OPTIONS, PATCH

### Proteções Implementadas

- ✅ **CSRF Disabled**: APIs REST usam tokens, não cookies
- ✅ **SQL Injection**: JPA previne automaticamente
- ✅ **XSS**: Validações de input + sanitização
- ✅ **Brute Force**: Keycloak tem rate limiting built-in
- ✅ **Token Expiration**: Access Token expira em 5 minutos
- ✅ **Senha Hashing**: Keycloak usa bcrypt automático
- ✅ **Validação de CPF**: Algoritmo completo de validação

---

## 💾 Banco de Dados

### Schema Automático

O Hibernate cria automaticamente as tabelas com base nas entidades JPA.

**Configuração:**
```properties
spring.jpa.hibernate.ddl-auto=update
```

**Modos disponíveis:**
- `create`: Recria o schema a cada inicialização (⚠️ perde dados)
- `create-drop`: Cria no início, destrói no fim
- `update`: Atualiza schema sem perder dados (✅ recomendado para dev)
- `validate`: Apenas valida se o schema está correto
- `none`: Não faz nada

### Tabela: `users`

```sql
CREATE TABLE users (
    id                  BIGSERIAL PRIMARY KEY,
    user_name           VARCHAR(100) NOT NULL UNIQUE,
    first_name          VARCHAR(100) NOT NULL,
    last_name           VARCHAR(100) NOT NULL,
    email               VARCHAR(255) NOT NULL UNIQUE,
    cpf                 VARCHAR(11) NOT NULL UNIQUE,
    keycloak_id         VARCHAR(255) NOT NULL UNIQUE,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_keycloak_id ON users(keycloak_id);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_cpf ON users(cpf);
```

### Tabela: `password_recovery_codes`

```sql
CREATE TABLE password_recovery_codes (
    id                  BIGSERIAL PRIMARY KEY,
    email               VARCHAR(255) NOT NULL,
    code                VARCHAR(6) NOT NULL,
    expires_at          TIMESTAMP NOT NULL,
    used                BOOLEAN DEFAULT FALSE,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_recovery_email_code ON password_recovery_codes(email, code);
```

### Diagrama ER

```
┌─────────────────────────┐
│        USERS            │
├─────────────────────────┤
│ id (PK)                 │
│ user_name (UNIQUE)      │
│ first_name              │
│ last_name               │
│ email (UNIQUE)          │
│ cpf (UNIQUE)            │
│ keycloak_id (UNIQUE)    │
│ created_at              │
│ updated_at              │
└─────────────────────────┘

┌───────────────────────────────┐
│  PASSWORD_RECOVERY_CODES      │
├───────────────────────────────┤
│ id (PK)                       │
│ email                         │
│ code                          │
│ expires_at                    │
│ used                          │
│ created_at                    │
└───────────────────────────────┘
```

### Acessar o Banco

```bash
# Via terminal
psql -U postgres -d personal_finance_db

# Ver tabelas
\dt

# Ver estrutura da tabela users
\d users

# Consultar usuários
SELECT id, user_name, email, created_at FROM users;
```

---

## 🧪 Testes

### Executar Todos os Testes

```bash
./mvnw test
```

### Executar Testes de uma Classe Específica

```bash
./mvnw test -Dtest=UserCreatorTest
```

### Cobertura de Testes

```bash
./mvnw clean test jacoco:report
```

Relatório gerado em: `target/site/jacoco/index.html`

### Tipos de Testes Implementados

#### 1. **Testes Unitários**
Testam classes isoladamente com mocks.

**Exemplo: UserMapperTest.java**
```java
@Test
void shouldMapUserToResponseDTO() {
    // Given
    User user = User.create("joao", "João", "Silva",
                           "joao@example.com", "12345678901", "kc-123");

    // When
    UserResponseDTO dto = UserMapper.toResponseDTO(user);

    // Then
    assertThat(dto.getUserName()).isEqualTo("joao");
    assertThat(dto.getEmail()).isEqualTo("joao@example.com");
}
```

#### 2. **Testes de Integração**
Testam o sistema completo (com banco de dados).

**Exemplo: UserCreatorTest.java**
```java
@SpringBootTest
@Transactional
class UserCreatorTest {

    @Autowired
    private UserCreator userCreator;

    @Test
    void shouldCreateUserSuccessfully() {
        // Given
        UserCreateRequestDTO dto = new UserCreateRequestDTO(...);

        // When
        userCreator.createUser(dto);

        // Then
        User user = userRepository.findByEmail(dto.getEmail());
        assertThat(user).isNotNull();
    }
}
```

#### 3. **Testes de Controller**
Testam endpoints HTTP.

```java
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnUnauthorizedWhenNoToken() throws Exception {
        mockMvc.perform(get("/users/me"))
               .andExpect(status().isUnauthorized());
    }
}
```

### Boas Práticas de Testes

- ✅ Use nomes descritivos: `shouldCreateUserWhenDataIsValid()`
- ✅ Siga padrão Given-When-Then
- ✅ Um assert por teste (quando possível)
- ✅ Testes independentes (não dependem de ordem)
- ✅ Use @Transactional para rollback automático

---

## 🔧 Resolução de Problemas

### Problema: Keycloak não inicia

**Sintomas:**
```
Error: Port 8081 already in use
```

**Solução:**
```bash
# Verificar o que está usando a porta
lsof -i :8081

# Matar o processo
kill -9 PID

# Ou mudar a porta do Keycloak
docker run -p 8090:8080 ...
# E atualizar application.properties
```

### Problema: PostgreSQL connection refused

**Sintomas:**
```
org.postgresql.util.PSQLException: Connection refused
```

**Soluções:**

1. **Verificar se o PostgreSQL está rodando:**
```bash
# Linux
sudo systemctl status postgresql

# Docker
docker ps | grep postgres
```

2. **Verificar credenciais:**
```bash
psql -U postgres -d personal_finance_db
```

3. **Verificar URL de conexão:**
```properties
# Deve ser exatamente assim
spring.datasource.url=jdbc:postgresql://localhost:5432/personal_finance_db
```

### Problema: Email não é enviado

**Sintomas:**
```
AuthenticationFailedException: 535-5.7.8 Username and Password not accepted
```

**Soluções:**

1. **Usar senha de aplicativo do Gmail** (não a senha normal)
2. **Ativar "Acesso a app menos seguro"** (não recomendado)
3. **Verificar configuração:**
```properties
spring.mail.username=seu-email@gmail.com
spring.mail.password=senha-de-aplicativo-16-caracteres
```

### Problema: Token JWT inválido

**Sintomas:**
```
401 Unauthorized - Invalid token
```

**Soluções:**

1. **Verificar issuer-uri:**
```properties
# Deve corresponder ao realm do Keycloak
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8081/realms/personal-finance-realm
```

2. **Token expirado:**
- Access Token expira em 5 minutos
- Use o refresh token para obter um novo

3. **Realm ou Client incorretos:**
- Verifique se o realm existe no Keycloak
- Verifique se o client está configurado corretamente

### Problema: Erro ao criar usuário

**Sintomas:**
```
DuplicateEmailException: Email já cadastrado
```

**Solução:**
- Email deve ser único
- Use outro email ou delete o usuário existente

**Sintomas:**
```
InvalidCpfException: CPF inválido
```

**Solução:**
- Use um CPF válido (algoritmo de validação completo)
- Exemplo válido: 12345678909

### Problema: Porta 8082 já em uso

**Solução:**
```bash
# Linux/Mac
lsof -i :8082
kill -9 PID

# Ou mudar a porta
server.port=8083
```

### Logs Úteis

**Ativar logs de SQL:**
```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
```

**Ativar logs de segurança:**
```properties
logging.level.org.springframework.security=DEBUG
```

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Siga os passos:

### 1. Fork o Projeto

```bash
# Clone seu fork
git clone https://github.com/seu-usuario/personal-finance.git
cd personal-finance
```

### 2. Crie uma Branch

```bash
git checkout -b feature/nova-funcionalidade
# ou
git checkout -b bugfix/correcao-bug
```

### 3. Faça suas Alterações

- Siga os padrões do projeto
- Escreva testes
- Documente o código

### 4. Commit

```bash
git add .
git commit -m "feat: adiciona nova funcionalidade X"
```

**Padrão de Commits (Conventional Commits):**
- `feat:` Nova funcionalidade
- `fix:` Correção de bug
- `docs:` Documentação
- `style:` Formatação de código
- `refactor:` Refatoração
- `test:` Testes
- `chore:` Tarefas de build/CI

### 5. Push e Pull Request

```bash
git push origin feature/nova-funcionalidade
```

Abra um Pull Request no GitHub com:
- Descrição clara do que foi feito
- Referência a issues relacionadas
- Screenshots (se aplicável)

### Padrões de Código

- ✅ Use Java 21+ features (records, pattern matching)
- ✅ Siga SOLID principles
- ✅ Escreva testes para novas funcionalidades
- ✅ Use nomes descritivos para variáveis e métodos
- ✅ Documente classes e métodos públicos
- ✅ Mantenha métodos pequenos (< 20 linhas idealmente)
- ✅ Use Lombok para reduzir boilerplate

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 📞 Contato e Suporte

- **Email**: carlosgarcianeto229@gmail.com
- **GitHub**: [@carlos-garcia](https://github.com/carlos-garcia)
- **Issues**: https://github.com/seu-usuario/personal-finance/issues

---

## 🎓 Recursos Adicionais

### Documentação Oficial

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Keycloak](https://www.keycloak.org/documentation)
- [PostgreSQL](https://www.postgresql.org/docs/)

### Livros Recomendados

- **Clean Architecture** - Robert C. Martin
- **Domain-Driven Design** - Eric Evans
- **Effective Java** - Joshua Bloch
- **Spring in Action** - Craig Walls

### Tutoriais

- [Documentação completa da arquitetura](README-MODULOS-USER-SHARED.md)
- [Guia de integração com Keycloak](https://www.keycloak.org/docs/latest/securing_apps/)

---

## 🗺️ Roadmap

### Versão 1.0 (Atual)
- ✅ Autenticação e autorização com Keycloak
- ✅ CRUD de usuários
- ✅ Recuperação de senha
- ✅ Envio de emails
- ✅ Validações completas

### Versão 1.1 (Em Desenvolvimento)
- ⏳ Módulo de transações financeiras
- ⏳ Categorização de despesas
- ⏳ Dashboard de resumo financeiro

### Versão 2.0 (Planejado)
- 📋 Relatórios e gráficos
- 📋 Metas de economia
- 📋 Controle de orçamento mensal
- 📋 Integração com bancos (Open Banking)
- 📋 Notificações push
- 📋 App mobile (React Native)

---

## ⭐ Agradecimentos

Agradecimentos especiais a todos que contribuíram com este projeto!

---

<div align="center">

**Desenvolvido com ❤️ e ☕ por Carlos Garcia**

Se este projeto foi útil, considere dar uma ⭐!

</div>
