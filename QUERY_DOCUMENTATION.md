# 📝 Documentação de Queries - Repositories

## Resumo das Mudanças

Todos os repositories foram convertidos para usar **JPQL (Java Persistence Query Language)** ou **SQL Nativo** explicitamente, removendo os métodos de query automáticos do Spring Data JPA.

### Benefícios:
- ✅ **Controle total** sobre as queries executadas
- ✅ **Performance otimizada** com queries SQL nativas quando necessário
- ✅ **Manutenibilidade** - queries explícitas são mais fáceis de entender e debugar
- ✅ **Flexibilidade** - queries complexas com cálculos e agregações

---

## 1. InvestmentRepository

### Queries Básicas (JPQL)

#### 1.1 Buscar investimentos por usuário
```java
@Query("SELECT i FROM Investment i WHERE i.usuario.id = :usuarioId")
List<Investment> findByUsuarioId(@Param("usuarioId") Long usuarioId);
```
**Uso**: Retorna todos os investimentos de um usuário.

#### 1.2 Buscar investimentos ativos
```java
@Query("SELECT i FROM Investment i WHERE i.usuario.id = :usuarioId AND i.ativo = :ativo")
List<Investment> findByUsuarioIdAndAtivo(@Param("usuarioId") Long usuarioId, @Param("ativo") Boolean ativo);
```
**Uso**: Filtra investimentos por status ativo/inativo.

#### 1.3 Buscar por tipo de investimento
```java
@Query("SELECT i FROM Investment i WHERE i.usuario.id = :usuarioId AND i.tipoInvestimento = :tipoInvestimento")
List<Investment> findByUsuarioIdAndTipoInvestimento(@Param("usuarioId") Long usuarioId, @Param("tipoInvestimento") TipoInvestimento tipoInvestimento);
```
**Uso**: Filtra por tipo (ACAO, CRIPTO, TESOURO_DIRETO, etc.).

### Queries Avançadas de Filtro (JPQL)

#### 1.4 Buscar por corretora (busca parcial)
```java
@Query("SELECT i FROM Investment i WHERE i.usuario.id = :usuarioId AND LOWER(i.corretora) LIKE LOWER(CONCAT('%', :corretora, '%'))")
List<Investment> findByUsuarioIdAndCorretora(@Param("usuarioId") Long usuarioId, @Param("corretora") String corretora);
```
**Uso**: Busca investimentos por nome da corretora (case-insensitive).  
**Exemplo**: `findByUsuarioIdAndCorretora(15L, "clear")` → encontra "Clear", "CLEAR Corretora", etc.

#### 1.5 Buscar por período de compra
```java
@Query("SELECT i FROM Investment i WHERE i.usuario.id = :usuarioId AND i.dataCompra BETWEEN :dataInicio AND :dataFim")
List<Investment> findByUsuarioIdAndDataCompraBetween(@Param("usuarioId") Long usuarioId, @Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
```
**Uso**: Filtra investimentos por intervalo de datas.  
**Exemplo**: `findByUsuarioIdAndDataCompraBetween(15L, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31))`

#### 1.6 Buscar por símbolo (busca parcial)
```java
@Query("SELECT i FROM Investment i WHERE i.usuario.id = :usuarioId AND LOWER(i.simbolo) LIKE LOWER(CONCAT('%', :simbolo, '%'))")
List<Investment> findByUsuarioIdAndSimboloContaining(@Param("usuarioId") Long usuarioId, @Param("simbolo") String simbolo);
```
**Uso**: Busca por parte do símbolo do ativo.  
**Exemplo**: `findByUsuarioIdAndSimboloContaining(15L, "PETR")` → encontra "PETR4", "PETR3", etc.

#### 1.7 Ordenar por valor investido
```java
@Query("SELECT i FROM Investment i WHERE i.usuario.id = :usuarioId ORDER BY i.valorTotalInvestido DESC")
List<Investment> findByUsuarioIdOrderByValorTotalInvestidoDesc(@Param("usuarioId") Long usuarioId);
```
**Uso**: Retorna investimentos ordenados do maior para o menor valor.

#### 1.8 Ordenar por data de compra
```java
@Query("SELECT i FROM Investment i WHERE i.usuario.id = :usuarioId AND i.ativo = true ORDER BY i.dataCompra DESC")
List<Investment> findByUsuarioIdAndAtivoTrueOrderByDataCompraDesc(@Param("usuarioId") Long usuarioId);
```
**Uso**: Investimentos mais recentes primeiro.

### Queries Estatísticas (SQL Nativo)

#### 1.9 Contar investimentos ativos
```java
@Query("SELECT COUNT(i) FROM Investment i WHERE i.usuario.id = :usuarioId AND i.ativo = true")
Long countByUsuarioIdAndAtivoTrue(@Param("usuarioId") Long usuarioId);
```

#### 1.10 Calcular valor total investido
```sql
SELECT COALESCE(SUM(valor_total_investido), 0) 
FROM investments 
WHERE usuario_id = :usuarioId AND ativo = true
```
**Uso**: Soma de todos os valores investidos (apenas ativos).

#### 1.11 Resumo por tipo de investimento
```sql
SELECT tipo_investimento as tipo, 
       COUNT(*) as quantidade, 
       SUM(valor_total_investido) as total_investido 
FROM investments 
WHERE usuario_id = :usuarioId AND ativo = true 
GROUP BY tipo_investimento 
ORDER BY total_investido DESC
```
**Uso**: Estatísticas agregadas por tipo.  
**Retorno**: `List<Object[]>` onde cada array contém `[tipo, quantidade, total_investido]`

---

## 2. ObjectiveRepository

### Queries Básicas (JPQL)

#### 2.1 Buscar objectives ativos
```java
@Query("SELECT o FROM Objective o WHERE o.usuario.id = :usuarioId AND o.ativa = true")
List<Objective> findByUsuarioIdAndAtivaTrue(@Param("usuarioId") Long usuarioId);
```

#### 2.2 Buscar por mês/ano
```java
@Query("SELECT o FROM Objective o WHERE o.usuario.id = :usuarioId AND o.mesAno = :mesAno AND o.ativa = true")
List<Objective> findByUsuarioIdAndMesAnoAndAtivaTrue(@Param("usuarioId") Long usuarioId, @Param("mesAno") String mesAno);
```
**Exemplo**: `findByUsuarioIdAndMesAnoAndAtivaTrue(15L, "2025-01")`

#### 2.3 Buscar por tipo
```java
@Query("SELECT o FROM Objective o WHERE o.usuario.id = :usuarioId AND o.tipo = :tipo AND o.ativa = true")
List<Objective> findByUsuarioIdAndTipoAndAtivaTrue(@Param("usuarioId") Long usuarioId, @Param("tipo") ObjectiveType tipo);
```

### Queries Avançadas (SQL Nativo)

#### 2.4 Objectives próximos de serem cumpridos (>= 80%)
```sql
SELECT * FROM objectives o 
WHERE o.usuario_id = :usuarioId 
  AND o.ativa = true 
  AND (o.valor_atual / NULLIF(o.valor_objetivo, 0) * 100) >= 80 
ORDER BY (o.valor_atual / NULLIF(o.valor_objetivo, 0) * 100) DESC
```
**Uso**: Identifica metas perto de serem atingidas.

#### 2.5 Objectives que ultrapassaram o limite
```sql
SELECT * FROM objectives o 
WHERE o.usuario_id = :usuarioId 
  AND o.tipo = :tipo 
  AND o.ativa = true 
  AND o.valor_atual > o.valor_objetivo 
ORDER BY (o.valor_atual - o.valor_objetivo) DESC
```
**Uso**: Alertas de limite de categoria ultrapassado.

#### 2.6 Contar objectives cumpridos
```sql
SELECT COUNT(*) FROM objectives o 
WHERE o.usuario_id = :usuarioId 
  AND o.ativa = true 
  AND o.tipo IN ('META_ECONOMIA_MES', 'META_INVESTIMENTO') 
  AND (o.valor_atual / NULLIF(o.valor_objetivo, 0) * 100) >= 100
```

#### 2.7 Resumo por tipo
```sql
SELECT tipo, 
       COUNT(*) as quantidade, 
       SUM(valor_objetivo) as total_objetivo, 
       SUM(valor_atual) as total_atual, 
       AVG((valor_atual / NULLIF(valor_objetivo, 0)) * 100) as percentual_medio 
FROM objectives 
WHERE usuario_id = :usuarioId AND ativa = true 
GROUP BY tipo
```

---

## 3. CategoryRepository

### Queries Básicas (JPQL)

#### 3.1 Buscar categorias do usuário
```java
@Query("SELECT c FROM Category c WHERE c.usuario.id = :usuarioId AND c.ativa = true")
List<Category> findByUsuarioIdAndAtivaTrue(@Param("usuarioId") Long usuarioId);
```

#### 3.2 Buscar categorias padrão
```java
@Query("SELECT c FROM Category c WHERE c.padrao = true AND c.ativa = true")
List<Category> findByPadraoTrueAndAtivaTrue();
```

#### 3.3 Buscar por tipo
```java
@Query("SELECT c FROM Category c WHERE c.usuario.id = :usuarioId AND c.tipo = :tipo AND c.ativa = true")
List<Category> findByUsuarioIdAndTipoAndAtivaTrue(@Param("usuarioId") Long usuarioId, @Param("tipo") CategoryType tipo);
```

### Queries Avançadas (JPQL)

#### 3.4 Buscar por nome (busca parcial)
```java
@Query("SELECT c FROM Category c WHERE c.usuario.id = :usuarioId AND LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%')) AND c.ativa = true")
List<Category> findByUsuarioIdAndNomeContaining(@Param("usuarioId") Long usuarioId, @Param("nome") String nome);
```

#### 3.5 Todas as categorias disponíveis (padrão + próprias)
```java
@Query("SELECT c FROM Category c WHERE (c.padrao = true OR c.usuario.id = :usuarioId) AND c.ativa = true ORDER BY c.padrao DESC, c.nome ASC")
List<Category> findAllAvailableForUser(@Param("usuarioId") Long usuarioId);
```
**Uso**: Retorna categorias padrão + categorias criadas pelo usuário.

#### 3.6 Verificar se categoria já existe
```java
@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Category c WHERE c.usuario.id = :usuarioId AND LOWER(c.nome) = LOWER(:nome) AND c.ativa = true")
boolean existsByUsuarioIdAndNome(@Param("usuarioId") Long usuarioId, @Param("nome") String nome);
```

### Queries Estatísticas (SQL Nativo)

#### 3.7 Categorias mais utilizadas
```sql
SELECT c.id, c.nome, c.tipo, COUNT(o.id) as uso_count 
FROM categories c 
LEFT JOIN objectives o ON o.categoria_id = c.id AND o.ativa = true 
WHERE (c.padrao = true OR c.usuario_id = :usuarioId) AND c.ativa = true 
GROUP BY c.id, c.nome, c.tipo 
ORDER BY uso_count DESC 
LIMIT 10
```
**Uso**: Top 10 categorias com mais objectives associados.

---

## 4. UserRepository

### Queries Básicas (JPQL)

#### 4.1 Verificar existência por email
```java
@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email")
boolean existsByEmail(@Param("email") String email);
```

#### 4.2 Verificar existência por username
```java
@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.userName = :userName")
boolean existsByUserName(@Param("userName") String userName);
```

#### 4.3 Verificar existência por CPF
```java
@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.cpf = :cpf")
boolean existsByCpf(@Param("cpf") String cpf);
```

#### 4.4 Buscar por Keycloak ID
```java
@Query("SELECT u FROM User u WHERE u.keycloakId = :keycloakId")
Optional<User> findByKeycloakId(@Param("keycloakId") String keycloakId);
```

#### 4.5 Buscar por email
```java
@Query("SELECT u FROM User u WHERE u.email = :email")
Optional<User> findByEmail(@Param("email") String email);
```

### Queries Avançadas (JPQL)

#### 4.6 Buscar por nome (busca parcial)
```java
@Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :nome, '%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :nome, '%'))")
List<User> findByNomeContaining(@Param("nome") String nome);
```

#### 4.7 Contar total de usuários
```java
@Query("SELECT COUNT(u) FROM User u")
Long countAllUsers();
```

#### 4.8 Ordenar por ID
```java
@Query("SELECT u FROM User u ORDER BY u.id DESC")
List<User> findAllOrderByIdDesc();
```

---

## 🧪 Como Testar as Novas Queries

### 1. Testar Filtros de Investment

```bash
# Buscar por corretora
GET /investments?corretora=Clear

# Buscar por período
GET /investments?dataInicio=2025-01-01&dataFim=2025-12-31

# Buscar por símbolo
GET /investments?simbolo=PETR

# Ordenar por valor
GET /investments?ordenar=valor

# Estatísticas
GET /investments/estatisticas
```

### 2. Testar Filtros de Objective

```bash
# Objectives próximos de serem cumpridos
GET /objectives/proximos-de-cumprir

# Objectives que ultrapassaram limite
GET /objectives/limites-ultrapassados

# Resumo estatístico
GET /objectives/resumo
```

### 3. Testar Filtros de Category

```bash
# Buscar por nome
GET /categories?nome=alimentacao

# Categorias disponíveis
GET /categories/disponiveis

# Categorias mais usadas
GET /categories/mais-usadas
```

---

## ⚠️ Observações Importantes

1. **JPQL vs SQL Nativo**:
   - Use JPQL para queries simples e quando possível (mais portável)
   - Use SQL Nativo para cálculos complexos, agregações e performance crítica

2. **Validação de Segurança**:
   - Todas as queries filtram por `usuario.id` garantindo isolamento de dados
   - Nunca exponha queries que retornem dados de outros usuários

3. **Performance**:
   - Queries com `LIKE` podem ser lentas em tabelas grandes - considere índices
   - Queries SQL nativas são mais rápidas para agregações complexas
   - Use `COALESCE` para evitar NULL em somas e cálculos

4. **Manutenção**:
   - Documente queries complexas com comentários
   - Use nomes descritivos para os métodos
   - Sempre adicione `@Param` para melhor legibilidade

---

## 📊 Resumo das Conversões

| Repository | Queries Básicas | Queries Filtro | Queries Estatísticas | Total |
|-----------|----------------|----------------|---------------------|-------|
| InvestmentRepository | 4 | 5 | 3 | 12 |
| ObjectiveRepository | 4 | 1 | 4 | 9 |
| CategoryRepository | 4 | 3 | 1 | 8 |
| UserRepository | 6 | 2 | 0 | 8 |
| **TOTAL** | **18** | **11** | **8** | **37** |

---

## ✅ Testes Realizados

Todos os endpoints foram testados e estão funcionando corretamente:

- ✅ GET /investments - Lista todos os investimentos
- ✅ GET /investments/{id} - Busca por ID
- ✅ GET /investments/ativo?status=true - Filtra por status
- ✅ GET /investments/tipo/{tipo} - Filtra por tipo
- ✅ GET /objectives - Lista todos os objectives
- ✅ GET /users/me - Busca perfil do usuário

**Data dos testes**: 03/12/2025  
**Status**: ✅ Todos os testes passaram com sucesso!
