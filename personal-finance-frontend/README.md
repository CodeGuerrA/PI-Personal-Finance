# FinTrack - Frontend Flutter

## Descrição

**FinTrack** (Sistema de Gestão Financeira Inteligente) é uma aplicação Flutter multiplataforma para gerenciamento financeiro pessoal. O aplicativo oferece uma interface moderna e intuitiva com suporte completo a tema claro/escuro, cache local para acesso offline, e integração com backend Spring Boot via API REST.

## Arquitetura

O projeto implementa **Clean Architecture** com estrutura modular baseada em features, garantindo separação clara de responsabilidades, testabilidade e escalabilidade.

### Camadas da Arquitetura

```
┌─────────────────────────────────────────────────────┐
│ PRESENTATION LAYER (UI)                             │
│ - Pages (telas)                                     │
│ - Providers (state management)                      │
│ - Widgets (componentes reutilizáveis)              │
└──────────────────────┬──────────────────────────────┘
                       ↓
┌─────────────────────────────────────────────────────┐
│ DOMAIN LAYER (Regras de Negócio)                   │
│ - Entities (modelos de negócio)                    │
│ - Repositories (interfaces abstratas)               │
│ - Use Cases (lógica de aplicação)                  │
└──────────────────────┬──────────────────────────────┘
                       ↓
┌─────────────────────────────────────────────────────┐
│ DATA LAYER (Acesso a Dados)                        │
│ - Models (serialização JSON)                       │
│ - Datasources (remote/local)                       │
│ - Repository Implementations                        │
└─────────────────────────────────────────────────────┘
```

### Estrutura de Features

Cada feature é organizada em 3 camadas independentes:

```
features/[feature]/
├── domain/              # Lógica de negócio
│   ├── entities/        # Modelos puros (sem dependências)
│   ├── repositories/    # Interfaces abstratas
│   └── usecases/        # Casos de uso
├── data/                # Implementação de dados
│   ├── models/          # Modelos com JSON serialização
│   ├── datasources/     # Remote/Local data sources
│   └── repositories/    # Implementações concretas
└── presentation/        # Interface do usuário
    ├── pages/           # Telas
    ├── providers/       # State management (Provider)
    └── widgets/         # Componentes customizados
```

## Tecnologias Utilizadas

### Core Framework
- **Flutter**: 3.x+ (Dart 3.5.0+)
- **Material Design 3**: Sistema de design moderno

### State Management
- **Provider**: 6.1.1 (gerenciamento de estado reativo)
- **ChangeNotifier**: Padrão Observer para notificações

### Networking
- **HTTP**: 1.2.0 (cliente HTTP para comunicação com API)
- **Base URL**: `http://localhost:8082` (Spring Boot backend)

### Storage & Cache
- **Flutter Secure Storage**: 9.0.0 (armazenamento seguro de tokens)
- **Hive**: 2.2.3 (banco de dados NoSQL local)
- **Hive Flutter**: 1.1.0 (integração Hive com Flutter)
- **SharedPreferences**: 2.2.2 (preferências do usuário)
- **Path Provider**: 2.1.1 (acesso ao sistema de arquivos)

### UI & Visualização
- **FL Chart**: 0.68.0 (gráficos e visualizações)
- **Cupertino Icons**: 1.0.8 (ícones iOS)
- **Intl**: 0.19.0 (internacionalização e formatação)

### Development Tools
- **Flutter Lints**: 4.0.0 (análise de código)
- **Build Runner**: 2.4.7 (geração de código)
- **Hive Generator**: 2.0.1 (geração de adapters Hive)

## Features Implementadas

### 1. Autenticação (Auth)
**8 telas completas de autenticação e gerenciamento de conta**

- **Splash Screen** - Tela inicial com logo e animação
- **Login** - Autenticação com usuário e senha
- **Register** - Registro de novos usuários
- **First Password** - Tela de primeira senha (senha temporária)
- **Define Password** - Definição de nova senha
- **Forgot Password** - Recuperação de senha por email
- **Verify Code** - Verificação de código de recuperação
- **Change Password** - Alteração de senha
- **Profile** - Gerenciamento de perfil do usuário

**Funcionalidades:**
- Integração completa com Keycloak (OAuth2/JWT)
- Armazenamento seguro de tokens (Flutter Secure Storage)
- Validação de formulários
- Recuperação de senha por email com código
- Cache de dados do usuário

### 2. Transações (Transactions)
**Gerenciamento completo de transações financeiras**

- Listagem de transações com paginação (20 itens/página)
- Criação, edição e exclusão de transações
- Filtros por tipo (Receita/Despesa)
- Filtros por método de pagamento (PIX, Cartão, Dinheiro, etc.)
- Busca por período
- Cache local com Hive

**Tipos de Transação:**
- **Receita** (Income)
- **Despesa** (Expense)

**Métodos de Pagamento:**
- PIX
- Cartão de Crédito
- Cartão de Débito
- Transferência Bancária
- Boleto
- Dinheiro

### 3. Categorias (Categories)
**Sistema de categorização de transações**

- Categorias padrão do sistema (não editáveis)
- Categorias customizadas do usuário
- Suporte a cores personalizadas (hex)
- Suporte a ícones
- Tipos: Receita ou Despesa
- Status ativo/inativo

### 4. Investimentos (Investments)
**Gerenciamento de portfólio de investimentos**

- Registro de investimentos por tipo
- Cálculo automático de rentabilidade
- Tracking de lucro/prejuízo
- Cálculo de ROI (Return on Investment)

**Tipos de Investimento:**
- Ações (Stocks)
- Fundos Imobiliários (FII)
- Criptomoedas
- Renda Fixa
- Tesouro Direto
- CDB

**Métricas Calculadas:**
- Valor total investido
- Valor atual
- Lucro/Prejuízo
- Percentual de rentabilidade

### 5. Movimentações de Investimentos (Investment Movements)
**Histórico de operações em investimentos**

- Registro de compras
- Registro de vendas
- Registro de dividendos
- Rastreamento de quantidade e valores

### 6. Metas Financeiras (Goals)
**Acompanhamento de objetivos financeiros**

- Definição de metas com valor objetivo
- Tracking de progresso em tempo real
- Cálculo de percentual atingido
- Alertas de status (Vermelho/Amarelo/Ok)
- Vinculação com categorias

**Tipos de Meta:**
- Limite de categoria
- Economia mensal
- Objetivo de investimento
- Pagamento de dívida

### 7. Transações Recorrentes (Recurrences)
**Gerenciamento de transações automáticas**

- Criação de transações recorrentes
- Frequências: Diária, Semanal, Mensal, Anual
- Cálculo automático da próxima data
- Suporte a data de início e fim
- Vinculação com categorias

### 8. Relatórios (Reports)
**Análise e visualização de dados financeiros**

- Gráficos interativos (FL Chart)
- Relatórios por período
- Análise de receitas e despesas
- Visualizações customizadas

### 9. Dashboard (Home)
**Painel principal com visão geral financeira**

- Resumo do mês atual
- Saldo, receitas e despesas
- Progresso de metas
- Alertas de recorrências próximas
- Últimas transações
- Navegação para módulos

## Estrutura de Pastas

```
lib/
├── main.dart                           # Entry point
├── app.dart                            # Root widget com providers
│
├── core/                               # Recursos compartilhados
│   ├── cache/
│   │   └── cache_service.dart          # Serviço de cache (Hive)
│   ├── network/
│   │   └── api_client.dart             # Cliente HTTP com auth
│   ├── storage/
│   │   └── token_storage.dart          # Storage seguro de tokens
│   ├── theme/
│   │   ├── app_theme.dart              # Temas light/dark
│   │   ├── app_colors.dart             # Paleta de cores
│   │   ├── app_text_styles.dart        # Tipografia
│   │   ├── design_tokens.dart          # Design system tokens
│   │   └── module_icons.dart           # Mapeamento de ícones
│   ├── routes/
│   │   └── app_routes.dart             # Rotas nomeadas
│   ├── utils/
│   │   ├── format_utils.dart           # Formatação (moeda, data)
│   │   └── responsive_helper.dart      # Responsividade
│   ├── widgets/
│   │   ├── custom_snackbar.dart        # Notificações
│   │   ├── password_input.dart         # Input de senha
│   │   └── state_widgets.dart          # Loading/Error states
│   ├── validators/
│   │   └── form_validators.dart        # Validação de formulários
│   ├── services/
│   │   └── provider_invalidation_service.dart
│   └── providers/
│       └── theme_provider.dart         # Gerenciamento de tema
│
└── features/                           # Módulos funcionais
    ├── auth/                           # Autenticação
    │   ├── domain/
    │   │   ├── entities/
    │   │   │   ├── user_entity.dart
    │   │   │   └── auth_tokens_entity.dart
    │   │   ├── repositories/
    │   │   │   └── auth_repository.dart
    │   │   └── usecases/
    │   ├── data/
    │   │   ├── models/
    │   │   │   └── user_model.dart
    │   │   ├── datasources/
    │   │   │   ├── auth_remote_datasource.dart
    │   │   │   └── auth_remote_datasource_impl.dart
    │   │   └── repositories/
    │   │       └── auth_repository_impl.dart
    │   └── presentation/
    │       ├── pages/
    │       │   ├── splash_screen.dart
    │       │   ├── login_screen.dart
    │       │   ├── register_screen.dart
    │       │   ├── first_password_screen.dart
    │       │   ├── define_password_screen.dart
    │       │   ├── forgot_password_screen.dart
    │       │   ├── verify_code_screen.dart
    │       │   ├── change_password_screen.dart
    │       │   └── profile_screen.dart
    │       └── providers/
    │           └── auth_provider.dart
    │
    ├── transactions/                   # Transações
    │   ├── domain/
    │   ├── data/
    │   └── presentation/
    │
    ├── categories/                     # Categorias
    │   ├── domain/
    │   ├── data/
    │   └── presentation/
    │
    ├── investments/                    # Investimentos
    │   ├── domain/
    │   ├── data/
    │   └── presentation/
    │
    ├── investment_movements/           # Movimentações
    │   ├── domain/
    │   ├── data/
    │   └── presentation/
    │
    ├── goals/                          # Metas
    │   ├── domain/
    │   ├── data/
    │   └── presentation/
    │
    ├── recurrences/                    # Recorrências
    │   ├── domain/
    │   ├── data/
    │   └── presentation/
    │
    ├── reports/                        # Relatórios
    │   ├── domain/
    │   ├── data/
    │   └── presentation/
    │
    └── home/                           # Dashboard
        └── presentation/
            └── pages/
                └── home_dashboard_screen.dart
```

## Pré-requisitos

### Obrigatórios
- **Flutter SDK**: 3.x ou superior
- **Dart SDK**: 3.5.0 ou superior
- **Backend**: Spring Boot API rodando em `http://localhost:8082`
- **Keycloak**: Para autenticação OAuth2

### Recomendados
- **VS Code** ou **Android Studio** (IDEs)
- **Flutter DevTools** (debugging)
- **Postman** ou **Insomnia** (teste de API)

### Plataformas Suportadas
- ✅ Windows
- ✅ Linux
- ✅ macOS
- ✅ Android
- ✅ iOS
- ✅ Web

## Instalação e Configuração

### 1. Clonar o Repositório

```bash
cd personal-finance-frontend
```

### 2. Instalar Dependências

```bash
flutter pub get
```

Este comando:
- Baixa todas as dependências do pubspec.yaml
- Gera arquivos necessários
- Prepara o projeto para execução

### 3. Configurar Backend URL

Edite o arquivo `lib/core/network/api_client.dart`:

```dart
class ApiClient {
  static const String baseUrl = 'http://localhost:8082'; // Altere se necessário

  // ...
}
```

**Ambientes:**
- **Desenvolvimento**: `http://localhost:8082`
- **Produção**: `https://api.seudominio.com`

### 4. Iniciar o Backend

Certifique-se de que o backend Spring Boot está rodando:

```bash
# Na pasta do backend
cd ../personal-finance
./mvnw spring-boot:run
```

Verifique se está acessível em: `http://localhost:8082`

### 5. Executar o Aplicativo

#### Windows
```bash
flutter run -d windows
```

#### Linux
```bash
flutter run -d linux
```

#### Android (Emulador ou Dispositivo)
```bash
flutter run -d android
```

#### iOS (apenas em macOS)
```bash
flutter run -d ios
```

#### Web
```bash
flutter run -d chrome
```

### 6. Build para Produção

#### Windows
```bash
flutter build windows --release
```

#### Linux
```bash
flutter build linux --release
```

#### Android APK
```bash
flutter build apk --release
```

#### Android App Bundle
```bash
flutter build appbundle --release
```

#### iOS
```bash
flutter build ios --release
```

#### Web
```bash
flutter build web --release
```

## Configuração do Ambiente

### Variáveis de Ambiente

Para diferentes ambientes, você pode criar arquivos de configuração:

```dart
// lib/core/config/environment.dart
class Environment {
  static const String apiUrl = String.fromEnvironment(
    'API_URL',
    defaultValue: 'http://localhost:8082',
  );
}
```

Usar ao executar:

```bash
flutter run --dart-define=API_URL=https://api.producao.com
```

## Sistema de Autenticação

### Fluxo de Login

1. **Usuário informa credenciais** → Tela de login
2. **AuthProvider faz requisição** → Backend Spring Boot
3. **Backend valida no Keycloak** → Retorna tokens JWT
4. **TokenStorage armazena** → Flutter Secure Storage
5. **ApiClient injeta token** → Todas as requisições seguintes
6. **Navegação para Home** → Dashboard principal

### Token Management

**Access Token:**
- Válido por 5 minutos
- Enviado no header: `Authorization: Bearer <token>`
- Renovado automaticamente com Refresh Token

**Refresh Token:**
- Válido por 30 minutos
- Usado para obter novo Access Token sem nova senha

### Segurança

- ✅ Tokens armazenados com criptografia (Flutter Secure Storage)
- ✅ Comunicação HTTPS em produção
- ✅ Validação de inputs em todos os formulários
- ✅ Sanitização de dados antes de enviar ao backend
- ✅ Logout limpa todos os tokens e cache

## Cache Local (Offline Support)

### Estratégia de Cache

O aplicativo utiliza **Hive** para cache local, permitindo:

- Acesso a dados sem conexão
- Performance melhorada
- Redução de chamadas à API

### Boxes Hive

6 boxes separados por feature:

```dart
// Boxes de cache
- transactions_cache
- categories_cache
- goals_cache
- investments_cache
- recurrences_cache
- user_data_cache
```

### Segregação por Usuário

Cache é segregado por username:

```dart
// Exemplo de key
final key = '${username}_transactions_2024_12';
```

Isso permite:
- Múltiplos usuários no mesmo dispositivo
- Dados isolados e seguros
- Cache independente por usuário

### Invalidação de Cache

```dart
// Invalidar cache ao fazer logout
await CacheService().clearAllCache();

// Invalidar cache específico
await CacheService().clearTransactionsCache(username);
```

## State Management (Provider)

### Providers Implementados

**Core Provider:**
```dart
ThemeProvider        // Gerenciamento de tema (light/dark/system)
```

**Feature Providers:**
```dart
AuthProvider                 // Autenticação e usuário
TransactionProvider          // Transações
CategoryProvider             // Categorias
GoalProvider                 // Metas
InvestmentProvider           // Investimentos
RecurrenceProvider           // Transações recorrentes
InvestmentMovementProvider   // Movimentações
```

### Uso de Providers

#### Ler dados:
```dart
// 1. Via Consumer (rebuilda quando há mudança)
Consumer<AuthProvider>(
  builder: (context, auth, child) {
    return Text(auth.user?.name ?? 'Usuário');
  },
)

// 2. Via Provider.of (leitura única)
final auth = Provider.of<AuthProvider>(context, listen: false);
auth.login(username, password);

// 3. Via context.read (leitura única - mais moderno)
context.read<AuthProvider>().logout();

// 4. Via context.watch (rebuilda quando há mudança)
final user = context.watch<AuthProvider>().user;
```

### Criando um Novo Provider

```dart
class ExemploProvider with ChangeNotifier {
  bool _isLoading = false;
  String? _errorMessage;

  bool get isLoading => _isLoading;
  String? get errorMessage => _errorMessage;

  Future<void> executarAcao() async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners(); // Notifica widgets

    try {
      // Lógica aqui
      await Future.delayed(Duration(seconds: 2));

      _isLoading = false;
      notifyListeners();
    } catch (e) {
      _errorMessage = e.toString();
      _isLoading = false;
      notifyListeners();
    }
  }
}
```

## Sistema de Temas

### Temas Disponíveis

- **Light Mode** (Claro)
- **Dark Mode** (Escuro)
- **System** (Segue configuração do sistema)

### Paleta de Cores

#### Cores Principais
```dart
Primary:   Indigo (#5B7CE3)
Secondary: Teal (#00C4B4)
Success:   Green (#22C893)
Error:     Red (#FF5C79)
Warning:   Amber (#FFAB40)
```

#### Cores Financeiras
```dart
Income:    Spring Green (#26E07F)
Expense:   Rose (#FF6B9D)
```

### Alternar Tema

```dart
// Via Provider
context.read<ThemeProvider>().toggleTheme();

// Definir tema específico
context.read<ThemeProvider>().setTheme(ThemeMode.dark);
```

### Acessar Cores do Tema

```dart
// Cores do tema atual
Theme.of(context).colorScheme.primary
Theme.of(context).colorScheme.onSurface
Theme.of(context).colorScheme.error

// Verificar tema atual
final isDark = Theme.of(context).brightness == Brightness.dark;
```

## Formatação e Internacionalização

### Formatação de Moeda

```dart
import 'package:sgfi/core/utils/format_utils.dart';

// Formatar valor em reais
double valor = 1234.56;
String formatado = valor.toCurrency();  // "R$ 1.234,56"

// Formato compacto
String compacto = valor.toCompactCurrency();  // "R$ 1,2K"
```

### Formatação de Datas

```dart
// Data curta
DateTime data = DateTime.now();
String curta = data.toShortDate();  // "09/12/2024"

// Data completa
String completa = data.toFullDate();  // "09 de dezembro de 2024"

// Data e hora
String dateTime = data.toFullDateTime();  // "09/12/2024 às 14:30"
```

## Rotas

### Rotas Disponíveis

```dart
// Autenticação
AppRoutes.splash           // '/'
AppRoutes.login            // '/login'
AppRoutes.register         // '/register'
AppRoutes.firstPassword    // '/first-password'
AppRoutes.forgotPassword   // '/forgot-password'

// Principal
AppRoutes.home             // '/home'
AppRoutes.profile          // '/profile'

// Features
AppRoutes.transactions     // '/transactions'
AppRoutes.categories       // '/categories'
AppRoutes.investments      // '/investments'
AppRoutes.goals            // '/goals'
AppRoutes.recurrences      // '/recurrences'
AppRoutes.reportsOverview  // '/reports-overview'
```

### Navegação

```dart
// Navegar para tela
Navigator.of(context).pushNamed(AppRoutes.transactions);

// Navegar e substituir
Navigator.of(context).pushReplacementNamed(AppRoutes.home);

// Navegar com argumentos
Navigator.of(context).pushNamed(
  AppRoutes.firstPassword,
  arguments: username,
);

// Voltar
Navigator.of(context).pop();
```

## Testes

### Executar Testes

```bash
# Todos os testes
flutter test

# Testes específicos
flutter test test/features/auth/auth_provider_test.dart

# Com coverage
flutter test --coverage

# Ver coverage HTML
genhtml coverage/lcov.info -o coverage/html
open coverage/html/index.html
```

### Estrutura de Testes

```
test/
├── core/
│   ├── network/
│   │   └── api_client_test.dart
│   └── utils/
│       └── format_utils_test.dart
├── features/
│   ├── auth/
│   │   ├── domain/
│   │   ├── data/
│   │   └── presentation/
│   │       └── providers/
│   │           └── auth_provider_test.dart
│   └── transactions/
│       └── ...
└── widget_test.dart
```

### Exemplo de Teste

```dart
import 'package:flutter_test/flutter_test.dart';

void main() {
  group('AuthProvider', () {
    late AuthProvider authProvider;

    setUp(() {
      authProvider = AuthProvider();
    });

    test('deve fazer login com sucesso', () async {
      // Arrange
      const username = 'usuario';
      const password = 'senha123';

      // Act
      final success = await authProvider.login(username, password);

      // Assert
      expect(success, true);
      expect(authProvider.isAuthenticated, true);
      expect(authProvider.user, isNotNull);
    });
  });
}
```

## Troubleshooting

### Problema: Erro ao conectar com backend

**Sintomas:**
```
SocketException: Failed to connect to localhost:8082
```

**Soluções:**

1. **Backend não está rodando:**
```bash
cd ../personal-finance
./mvnw spring-boot:run
```

2. **URL incorreta:**
   - Android Emulator: Use `10.0.2.2:8082` ao invés de `localhost:8082`
   - iOS Simulator: Use IP da máquina ao invés de `localhost`

3. **Firewall bloqueando:**
   - Verifique configurações de firewall
   - Permita conexões na porta 8082

### Problema: Tokens expirados

**Sintomas:**
```
401 Unauthorized
```

**Solução:**
```dart
// O AuthProvider faz refresh automático
// Se não funcionar, fazer logout e login novamente
await context.read<AuthProvider>().logout();
```

### Problema: Flutter pub get falha

**Sintomas:**
```
Version solving failed
```

**Solução:**
```bash
# Limpar cache
flutter clean
flutter pub get

# Atualizar Flutter
flutter upgrade
```

### Problema: Build falha no Android

**Sintomas:**
```
Gradle build failed
```

**Soluções:**

1. **Limpar build:**
```bash
cd android
./gradlew clean
cd ..
flutter clean
flutter pub get
```

2. **Java version incompatível:**
   - Instale JDK 17
   - Configure JAVA_HOME

### Problema: Erro de permissão no iOS

**Sintomas:**
```
Missing permissions in Info.plist
```

**Solução:**
Adicione permissões no `ios/Runner/Info.plist`:

```xml
<key>NSPhotoLibraryUsageDescription</key>
<string>Precisamos acessar suas fotos</string>
```

## Performance

### Otimizações Implementadas

- ✅ **Lazy Loading**: Páginas carregadas sob demanda
- ✅ **Cache Local**: Redução de chamadas à API
- ✅ **Paginação**: Transações carregadas em blocos de 20
- ✅ **Image Caching**: Logos e imagens cacheados
- ✅ **const Constructors**: Widgets constantes quando possível
- ✅ **ListView.builder**: Listas virtualizadas

### Dicas de Performance

```dart
// ✅ Bom: const quando possível
const Text('Título')

// ❌ Ruim: sem const
Text('Título')

// ✅ Bom: ListView.builder para listas grandes
ListView.builder(
  itemCount: items.length,
  itemBuilder: (context, index) => ListTile(title: Text(items[index])),
)

// ❌ Ruim: ListView com children para muitos itens
ListView(children: items.map((i) => ListTile(title: Text(i))).toList())
```

## Contribuição

### Como Contribuir

1. **Fork o projeto**
2. **Crie uma branch** para sua feature:
   ```bash
   git checkout -b feature/nova-funcionalidade
   ```
3. **Commit suas mudanças:**
   ```bash
   git commit -m "feat: adiciona nova funcionalidade"
   ```
4. **Push para a branch:**
   ```bash
   git push origin feature/nova-funcionalidade
   ```
5. **Abra um Pull Request**

### Convenção de Commits

```
feat:     Nova funcionalidade
fix:      Correção de bug
docs:     Documentação
style:    Formatação de código
refactor: Refatoração
test:     Testes
chore:    Tarefas de build/CI
```

### Padrões de Código

- ✅ Siga as convenções do Dart/Flutter
- ✅ Use `flutter analyze` antes de commitar
- ✅ Execute testes: `flutter test`
- ✅ Documente classes e métodos públicos
- ✅ Mantenha arquivos < 300 linhas quando possível
- ✅ Use nomes descritivos para variáveis e funções
- ✅ Evite código duplicado

### Code Review Checklist

- [ ] Código segue convenções Dart/Flutter
- [ ] Sem warnings do `flutter analyze`
- [ ] Testes passam com sucesso
- [ ] Documentação atualizada
- [ ] UI responsiva em diferentes tamanhos
- [ ] Tema dark/light funcionando
- [ ] Performance adequada
- [ ] Sem memory leaks

## Recursos Adicionais

### Documentação Oficial

- [Flutter](https://flutter.dev/docs)
- [Dart](https://dart.dev/guides)
- [Provider](https://pub.dev/packages/provider)
- [Hive](https://docs.hivedb.dev/)

### Tutoriais Recomendados

- [Clean Architecture in Flutter](https://resocoder.com/flutter-clean-architecture-tdd/)
- [Provider State Management](https://flutter.dev/docs/development/data-and-backend/state-mgmt/simple)
- [Flutter Performance Best Practices](https://flutter.dev/docs/perf/best-practices)

## Roadmap

### Versão 1.0 (Atual)
- ✅ Autenticação completa com JWT
- ✅ CRUD de transações
- ✅ Categorias e metas
- ✅ Investimentos e recorrências
- ✅ Dashboard com resumo
- ✅ Tema claro/escuro
- ✅ Cache local

### Versão 1.1 (Planejado)
- 📋 Gráficos avançados
- 📋 Exportação de relatórios (PDF/Excel)
- 📋 Notificações push
- 📋 Biometria para login
- 📋 Suporte a múltiplas moedas
- 📋 Backup na nuvem

### Versão 2.0 (Futuro)
- 📋 Integração com Open Banking
- 📋 Reconhecimento de voz
- 📋 IA para análise financeira
- 📋 Modo família (multi-usuário)
- 📋 Gamificação

## Licença

Este projeto é de uso acadêmico e educacional.

---

## Contato

Para dúvidas ou sugestões:
- **Email**: carlosgarcianeto229@gmail.com
- **GitHub**: [@carlos-garcia](https://github.com/carlos-garcia)

---

<div align="center">

**Desenvolvido com Flutter e Clean Architecture**

Se este projeto foi útil, considere dar uma ⭐!

</div>
