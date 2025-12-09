import 'package:hive_flutter/hive_flutter.dart';
import 'package:sgfi/features/transactions/domain/entities/transaction_entity.dart';
import 'package:sgfi/features/categories/domain/entities/category_entity.dart';
import 'package:sgfi/features/goals/domain/entities/goal_entity.dart';
import 'package:sgfi/features/investments/domain/entities/investment_entity.dart';
import 'package:sgfi/features/recurrences/domain/entities/recurrence_entity.dart';

/// Serviço de cache local usando Hive
/// Armazena dados para acesso offline e melhor performance
/// Agora suporta cache separado por usuário
class CacheService {
  static const String _transactionsBox = 'transactions';
  static const String _categoriesBox = 'categories';
  static const String _goalsBox = 'goals';
  static const String _investmentsBox = 'investments';
  static const String _recurrencesBox = 'recurrences';
  static const String _userDataBox = 'userData';

  /// Usuário atual (username) para diferenciar cache
  static String? _currentUsername;

  /// Inicializa o Hive e abre as boxes
  static Future<void> init() async {
    await Hive.initFlutter();

    // Registrar adapters customizados aqui se necessário
    // Hive.registerAdapter(TransactionAdapter());

    // Abrir boxes
    await Hive.openBox(_transactionsBox);
    await Hive.openBox(_categoriesBox);
    await Hive.openBox(_goalsBox);
    await Hive.openBox(_investmentsBox);
    await Hive.openBox(_recurrencesBox);
    await Hive.openBox(_userDataBox);

    // Limpar cache antigo (dados sem prefixo de usuário)
    await _cleanOldCacheFormat();
  }

  /// Remove dados antigos do cache que não têm prefixo de usuário
  /// Este método é necessário para migração de versões antigas
  static Future<void> _cleanOldCacheFormat() async {
    print('🧹 CacheService - Limpando cache antigo (dados sem prefixo de usuário)...');

    final oldKeys = [
      'all_transactions',
      'last_sync',
      'all_categories',
      'last_sync_categories',
      'all_goals',
      'last_sync_goals',
      'all_investments',
      'last_sync_investments',
      'all_recurrences',
      'last_sync_recurrences',
    ];

    int deletedCount = 0;

    // Limpar dos boxes
    for (var key in oldKeys) {
      if (transactionsBox.containsKey(key)) {
        await transactionsBox.delete(key);
        deletedCount++;
      }
      if (categoriesBox.containsKey(key)) {
        await categoriesBox.delete(key);
        deletedCount++;
      }
      if (goalsBox.containsKey(key)) {
        await goalsBox.delete(key);
        deletedCount++;
      }
      if (investmentsBox.containsKey(key)) {
        await investmentsBox.delete(key);
        deletedCount++;
      }
      if (recurrencesBox.containsKey(key)) {
        await recurrencesBox.delete(key);
        deletedCount++;
      }
    }

    print('✅ CacheService - Limpeza concluída! $deletedCount chaves antigas removidas');
  }

  /// Box de transações
  static Box get transactionsBox => Hive.box(_transactionsBox);

  /// Box de categorias
  static Box get categoriesBox => Hive.box(_categoriesBox);

  /// Box de metas
  static Box get goalsBox => Hive.box(_goalsBox);

  /// Box de investimentos
  static Box get investmentsBox => Hive.box(_investmentsBox);

  /// Box de recorrências
  static Box get recurrencesBox => Hive.box(_recurrencesBox);

  /// Box de dados do usuário
  static Box get userDataBox => Hive.box(_userDataBox);

  /// Define o usuário atual para o cache
  static void setCurrentUsername(String? username) {
    print('🔐 CacheService - Configurando usuário: ${username ?? "null"}');
    _currentUsername = username;
  }

  /// Obtém o usuário atual
  static String? getCurrentUsername() {
    return _currentUsername;
  }

  /// Gera chave de cache com prefixo do usuário
  static String _getUserKey(String key) {
    if (_currentUsername == null || _currentUsername!.isEmpty) {
      return key;
    }
    return '${_currentUsername}_$key';
  }

  // ========== TRANSAÇÕES ==========

  /// Salva lista de transações no cache
  static Future<void> cacheTransactions(List<TransactionEntity> transactions) async {
    final box = transactionsBox;
    final data = transactions.map((t) => {
      'id': t.id,
      'description': t.description,
      'amount': t.amount,
      'date': t.date.toIso8601String(),
      'category': t.category,
      'type': t.type.toString(),
      'paymentMethod': t.paymentMethod.toString(),
      'observacoes': t.observacoes,
      'categoriaId': t.categoriaId,
    }).toList();

    await box.put(_getUserKey('all_transactions'), data);
    await box.put(_getUserKey('last_sync'), DateTime.now().toIso8601String());
  }

  /// Obtém transações do cache
  static List<Map<String, dynamic>>? getCachedTransactions() {
    // Segurança: não retornar cache se não houver usuário configurado
    if (_currentUsername == null || _currentUsername!.isEmpty) {
      return null;
    }

    final box = transactionsBox;
    final data = box.get(_getUserKey('all_transactions'));
    if (data == null) return null;
    return List<Map<String, dynamic>>.from(data);
  }

  /// Verifica se o cache de transações está atualizado (< 5 minutos)
  static bool isTransactionsCacheValid() {
    final box = transactionsBox;
    final lastSync = box.get(_getUserKey('last_sync'));
    if (lastSync == null) return false;

    final lastSyncDate = DateTime.parse(lastSync);
    final now = DateTime.now();
    final difference = now.difference(lastSyncDate);

    return difference.inMinutes < 5; // Cache válido por 5 minutos
  }

  /// Limpa cache de transações
  static Future<void> clearTransactionsCache() async {
    await transactionsBox.clear();
  }

  // ========== CATEGORIAS ==========

  /// Salva lista de categorias no cache
  static Future<void> cacheCategories(List<CategoryEntity> categories) async {
    final box = categoriesBox;
    final data = categories.map((c) => {
      'id': c.id,
      'name': c.name,
      'isDefault': c.isDefault,
      'isIncome': c.isIncome,
      'cor': c.cor,
      'icone': c.icone,
      'ativa': c.ativa,
    }).toList();

    await box.put(_getUserKey('all_categories'), data);
    await box.put(_getUserKey('last_sync_categories'), DateTime.now().toIso8601String());
  }

  /// Obtém categorias do cache
  static List<Map<String, dynamic>>? getCachedCategories() {
    // Segurança: não retornar cache se não houver usuário configurado
    if (_currentUsername == null || _currentUsername!.isEmpty) {
      return null;
    }

    final box = categoriesBox;
    final data = box.get(_getUserKey('all_categories'));
    if (data == null) return null;
    return List<Map<String, dynamic>>.from(data);
  }

  /// Verifica se o cache de categorias está atualizado
  static bool isCategoriesCacheValid() {
    final box = categoriesBox;
    final lastSync = box.get(_getUserKey('last_sync_categories'));
    if (lastSync == null) return false;

    final lastSyncDate = DateTime.parse(lastSync);
    final now = DateTime.now();
    final difference = now.difference(lastSyncDate);

    return difference.inMinutes < 30; // Cache válido por 30 minutos
  }

  /// Limpa cache de categorias
  static Future<void> clearCategoriesCache() async {
    await categoriesBox.clear();
  }

  // ========== METAS ==========

  /// Salva lista de metas no cache
  static Future<void> cacheGoals(List<GoalEntity> goals) async {
    final box = goalsBox;
    final data = goals.map((g) => {
      'id': g.id,
      'name': g.name,
      'targetAmount': g.targetAmount,
      'currentAmount': g.currentAmount,
      'dueDate': g.dueDate?.toIso8601String(),
      'type': g.type?.toString(),
      'isActive': g.isActive,
      'percentualAtingido': g.percentualAtingido,
      'saldoRestante': g.saldoRestante,
      'statusAlerta': g.statusAlerta,
    }).toList();

    await box.put(_getUserKey('all_goals'), data);
    await box.put(_getUserKey('last_sync_goals'), DateTime.now().toIso8601String());
  }

  /// Obtém metas do cache
  static List<Map<String, dynamic>>? getCachedGoals() {
    // Segurança: não retornar cache se não houver usuário configurado
    if (_currentUsername == null || _currentUsername!.isEmpty) {
      return null;
    }

    final box = goalsBox;
    final data = box.get(_getUserKey('all_goals'));
    if (data == null) return null;
    return List<Map<String, dynamic>>.from(data);
  }

  /// Verifica se o cache de metas está atualizado
  static bool isGoalsCacheValid() {
    final box = goalsBox;
    final lastSync = box.get(_getUserKey('last_sync_goals'));
    if (lastSync == null) return false;

    final lastSyncDate = DateTime.parse(lastSync);
    final now = DateTime.now();
    final difference = now.difference(lastSyncDate);

    return difference.inMinutes < 10; // Cache válido por 10 minutos
  }

  /// Limpa cache de metas
  static Future<void> clearGoalsCache() async {
    await goalsBox.clear();
  }

  // ========== INVESTIMENTOS ==========

  /// Salva lista de investimentos no cache
  static Future<void> cacheInvestments(List<InvestmentEntity> investments) async {
    final box = investmentsBox;
    final data = investments.map((i) => {
      'id': i.id,
      'name': i.name,
      'symbol': i.symbol,
      'type': i.type.toString(),
      'quantity': i.quantity,
      'buyPrice': i.buyPrice,
      'currentPrice': i.currentPrice,
      'broker': i.broker,
      'isActive': i.isActive,
      'totalInvested': i.totalInvested,
      'currentValue': i.currentValue,
      'profit': i.profit,
      'profitPercent': i.profitPercent,
    }).toList();

    await box.put(_getUserKey('all_investments'), data);
    await box.put(_getUserKey('last_sync_investments'), DateTime.now().toIso8601String());
  }

  /// Obtém investimentos do cache
  static List<Map<String, dynamic>>? getCachedInvestments() {
    // Segurança: não retornar cache se não houver usuário configurado
    if (_currentUsername == null || _currentUsername!.isEmpty) {
      return null;
    }

    final box = investmentsBox;
    final data = box.get(_getUserKey('all_investments'));
    if (data == null) return null;
    return List<Map<String, dynamic>>.from(data);
  }

  /// Verifica se o cache de investimentos está atualizado
  static bool isInvestmentsCacheValid() {
    final box = investmentsBox;
    final lastSync = box.get(_getUserKey('last_sync_investments'));
    if (lastSync == null) return false;

    final lastSyncDate = DateTime.parse(lastSync);
    final now = DateTime.now();
    final difference = now.difference(lastSyncDate);

    return difference.inMinutes < 15; // Cache válido por 15 minutos
  }

  /// Limpa cache de investimentos
  static Future<void> clearInvestmentsCache() async {
    await investmentsBox.clear();
  }

  // ========== RECORRÊNCIAS ==========

  /// Salva lista de recorrências no cache
  static Future<void> cacheRecurrences(List<RecurrenceEntity> recurrences) async {
    final box = recurrencesBox;
    final data = recurrences.map((r) => {
      'id': r.id,
      'name': r.name,
      'amount': r.amount,
      'frequency': r.frequency.toString(),
      'type': r.type.toString(),
      'startDate': r.startDate.toIso8601String(),
      'endDate': r.endDate?.toIso8601String(),
      'categoryName': r.categoryName,
      'categoriaId': r.categoriaId,
      'isActive': r.isActive,
      'diaVencimento': r.diaVencimento,
      'observacoes': r.observacoes,
    }).toList();

    await box.put(_getUserKey('all_recurrences'), data);
    await box.put(_getUserKey('last_sync_recurrences'), DateTime.now().toIso8601String());
  }

  /// Obtém recorrências do cache
  static List<Map<String, dynamic>>? getCachedRecurrences() {
    // Segurança: não retornar cache se não houver usuário configurado
    if (_currentUsername == null || _currentUsername!.isEmpty) {
      return null;
    }

    final box = recurrencesBox;
    final data = box.get(_getUserKey('all_recurrences'));
    if (data == null) return null;
    return List<Map<String, dynamic>>.from(data);
  }

  /// Verifica se o cache de recorrências está atualizado
  static bool isRecurrencesCacheValid() {
    final box = recurrencesBox;
    final lastSync = box.get(_getUserKey('last_sync_recurrences'));
    if (lastSync == null) return false;

    final lastSyncDate = DateTime.parse(lastSync);
    final now = DateTime.now();
    final difference = now.difference(lastSyncDate);

    return difference.inMinutes < 20; // Cache válido por 20 minutos
  }

  /// Limpa cache de recorrências
  static Future<void> clearRecurrencesCache() async {
    await recurrencesBox.clear();
  }

  // ========== GERAL ==========

  /// Limpa apenas o cache do usuário atual (mantém dados de outros usuários)
  static Future<void> clearCurrentUserCache() async {
    if (_currentUsername == null || _currentUsername!.isEmpty) {
      print('⚠️  CacheService - Nenhum usuário configurado, limpando todo o cache');
      // Se não há usuário definido, limpar tudo para segurança
      await clearAllCache();
      return;
    }

    print('🗑️  CacheService - Limpando cache do usuário: $_currentUsername');

    // Limpar apenas as chaves do usuário atual
    final userPrefix = '${_currentUsername}_';

    await _clearUserKeysFromBox(transactionsBox, userPrefix);
    await _clearUserKeysFromBox(categoriesBox, userPrefix);
    await _clearUserKeysFromBox(goalsBox, userPrefix);
    await _clearUserKeysFromBox(investmentsBox, userPrefix);
    await _clearUserKeysFromBox(recurrencesBox, userPrefix);

    print('✅ CacheService - Cache do usuário $_currentUsername limpo');
  }

  /// Limpa chaves específicas de um box baseado no prefixo do usuário
  static Future<void> _clearUserKeysFromBox(Box box, String userPrefix) async {
    final keysToDelete = <dynamic>[];

    for (var key in box.keys) {
      if (key.toString().startsWith(userPrefix)) {
        keysToDelete.add(key);
      }
    }

    for (var key in keysToDelete) {
      await box.delete(key);
    }
  }

  /// Limpa todo o cache (todos os usuários)
  static Future<void> clearAllCache() async {
    await transactionsBox.clear();
    await categoriesBox.clear();
    await goalsBox.clear();
    await investmentsBox.clear();
    await recurrencesBox.clear();
    await userDataBox.clear();
  }

  /// Fecha todas as boxes (chamar ao sair do app)
  static Future<void> close() async {
    await Hive.close();
  }
}
