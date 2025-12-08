import 'package:hive_flutter/hive_flutter.dart';
import 'package:sgfi/features/transactions/domain/entities/transaction_entity.dart';
import 'package:sgfi/features/categories/domain/entities/category_entity.dart';
import 'package:sgfi/features/goals/domain/entities/goal_entity.dart';
import 'package:sgfi/features/investments/domain/entities/investment_entity.dart';
import 'package:sgfi/features/recurrences/domain/entities/recurrence_entity.dart';

/// Serviço de cache local usando Hive
/// Armazena dados para acesso offline e melhor performance
class CacheService {
  static const String _transactionsBox = 'transactions';
  static const String _categoriesBox = 'categories';
  static const String _goalsBox = 'goals';
  static const String _investmentsBox = 'investments';
  static const String _recurrencesBox = 'recurrences';
  static const String _userDataBox = 'userData';

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

    await box.put('all_transactions', data);
    await box.put('last_sync', DateTime.now().toIso8601String());
  }

  /// Obtém transações do cache
  static List<Map<String, dynamic>>? getCachedTransactions() {
    final box = transactionsBox;
    final data = box.get('all_transactions');
    if (data == null) return null;
    return List<Map<String, dynamic>>.from(data);
  }

  /// Verifica se o cache de transações está atualizado (< 5 minutos)
  static bool isTransactionsCacheValid() {
    final box = transactionsBox;
    final lastSync = box.get('last_sync');
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

    await box.put('all_categories', data);
    await box.put('last_sync', DateTime.now().toIso8601String());
  }

  /// Obtém categorias do cache
  static List<Map<String, dynamic>>? getCachedCategories() {
    final box = categoriesBox;
    final data = box.get('all_categories');
    if (data == null) return null;
    return List<Map<String, dynamic>>.from(data);
  }

  /// Verifica se o cache de categorias está atualizado
  static bool isCategoriesCacheValid() {
    final box = categoriesBox;
    final lastSync = box.get('last_sync');
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

    await box.put('all_goals', data);
    await box.put('last_sync', DateTime.now().toIso8601String());
  }

  /// Obtém metas do cache
  static List<Map<String, dynamic>>? getCachedGoals() {
    final box = goalsBox;
    final data = box.get('all_goals');
    if (data == null) return null;
    return List<Map<String, dynamic>>.from(data);
  }

  /// Verifica se o cache de metas está atualizado
  static bool isGoalsCacheValid() {
    final box = goalsBox;
    final lastSync = box.get('last_sync');
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

    await box.put('all_investments', data);
    await box.put('last_sync', DateTime.now().toIso8601String());
  }

  /// Obtém investimentos do cache
  static List<Map<String, dynamic>>? getCachedInvestments() {
    final box = investmentsBox;
    final data = box.get('all_investments');
    if (data == null) return null;
    return List<Map<String, dynamic>>.from(data);
  }

  /// Verifica se o cache de investimentos está atualizado
  static bool isInvestmentsCacheValid() {
    final box = investmentsBox;
    final lastSync = box.get('last_sync');
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

    await box.put('all_recurrences', data);
    await box.put('last_sync', DateTime.now().toIso8601String());
  }

  /// Obtém recorrências do cache
  static List<Map<String, dynamic>>? getCachedRecurrences() {
    final box = recurrencesBox;
    final data = box.get('all_recurrences');
    if (data == null) return null;
    return List<Map<String, dynamic>>.from(data);
  }

  /// Verifica se o cache de recorrências está atualizado
  static bool isRecurrencesCacheValid() {
    final box = recurrencesBox;
    final lastSync = box.get('last_sync');
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

  /// Limpa todo o cache
  static Future<void> clearAllCache() async {
    await clearTransactionsCache();
    await clearCategoriesCache();
    await clearGoalsCache();
    await clearInvestmentsCache();
    await clearRecurrencesCache();
    await userDataBox.clear();
  }

  /// Fecha todas as boxes (chamar ao sair do app)
  static Future<void> close() async {
    await Hive.close();
  }
}
