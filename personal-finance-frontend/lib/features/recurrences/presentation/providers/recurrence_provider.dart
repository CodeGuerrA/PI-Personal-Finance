import 'package:flutter/material.dart';
import 'package:sgfi/features/recurrences/domain/entities/recurrence_entity.dart';
import 'package:sgfi/features/recurrences/domain/repositories/recurrence_repository.dart';
import 'package:sgfi/features/transactions/domain/entities/transaction_entity.dart';
import 'package:sgfi/core/cache/cache_service.dart';

/// Provider para gerenciar estado de transações recorrentes
class RecurrenceProvider extends ChangeNotifier {
  final RecurrenceRepository _repository;

  RecurrenceProvider(this._repository);

  // Estado
  List<RecurrenceEntity> _recurrences = [];
  bool _isLoading = false;
  String? _errorMessage;

  // Getters
  List<RecurrenceEntity> get recurrences => _recurrences;
  bool get isLoading => _isLoading;
  String? get errorMessage => _errorMessage;

  /// Recorrências ativas
  List<RecurrenceEntity> get activeRecurrences =>
      _recurrences.where((r) => r.isActive).toList();

  /// Recorrências inativas
  List<RecurrenceEntity> get inactiveRecurrences =>
      _recurrences.where((r) => !r.isActive).toList();

  /// Recorrências de receita
  List<RecurrenceEntity> get incomeRecurrences =>
      _recurrences.where((r) => r.type == TransactionType.income).toList();

  /// Recorrências de despesa
  List<RecurrenceEntity> get expenseRecurrences =>
      _recurrences.where((r) => r.type == TransactionType.expense).toList();

  /// Recorrências próximas (7 dias)
  List<RecurrenceEntity> get upcomingRecurrences {
    final now = DateTime.now();
    final today = DateTime(now.year, now.month, now.day);
    final limit = today.add(const Duration(days: 7));

    final list = _recurrences.where((r) {
      final d = r.nextDueDate;
      if (d == null) return false;
      return !d.isBefore(today) && d.isBefore(limit);
    }).toList();

    list.sort((a, b) {
      final ad = a.nextDueDate;
      final bd = b.nextDueDate;
      if (ad == null && bd == null) return 0;
      if (ad == null) return 1;
      if (bd == null) return -1;
      return ad.compareTo(bd);
    });

    return list;
  }

  /// Recorrências vencidas (não pagas)
  List<RecurrenceEntity> get overdueRecurrences {
    final now = DateTime.now();
    final today = DateTime(now.year, now.month, now.day);

    return _recurrences.where((r) {
      final d = r.nextDueDate;
      if (d == null) return false;
      return d.isBefore(today);
    }).toList();
  }

  /// Total mensal de receitas recorrentes
  double get monthlyIncomeTotal {
    return _calculateMonthlyTotal(incomeRecurrences);
  }

  /// Total mensal de despesas recorrentes
  double get monthlyExpenseTotal {
    return _calculateMonthlyTotal(expenseRecurrences);
  }

  /// Saldo mensal de recorrências
  double get monthlyBalance {
    return monthlyIncomeTotal - monthlyExpenseTotal;
  }

  /// Carrega recorrências
  Future<void> loadRecurrences({bool refresh = false}) async {
    if (_isLoading) return;

    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      // Tentar cache primeiro se não for refresh forçado
      if (!refresh) {
        final cachedData = CacheService.getCachedRecurrences();
        if (cachedData != null && CacheService.isRecurrencesCacheValid()) {
          _recurrences = _convertCachedToEntities(cachedData);
          _isLoading = false;
          notifyListeners();
          return;
        }
      }

      // Buscar do backend
      final recurrences = await _repository.getActiveRecurrences();

      // Salvar no cache
      await CacheService.cacheRecurrences(recurrences);

      _recurrences = recurrences;
      _isLoading = false;
      notifyListeners();
    } catch (e) {
      _errorMessage = 'Erro ao carregar recorrências: ${e.toString()}';
      _isLoading = false;

      // Fallback: tentar usar cache expirado em caso de erro de rede
      if (!refresh) {
        final cachedData = CacheService.getCachedRecurrences();
        if (cachedData != null) {
          _recurrences = _convertCachedToEntities(cachedData);
        }
      }

      notifyListeners();
    }
  }

  /// Cria nova recorrência
  Future<bool> createRecurrence({
    required double valor,
    required String tipo,
    required int categoriaId,
    required String descricao,
    required DateTime dataInicio,
    required String frequencia,
    DateTime? dataFim,
    int? diaVencimento,
    String? observacoes,
  }) async {
    try {
      await _repository.createRecurrence(
        valor: valor,
        tipo: tipo,
        categoriaId: categoriaId,
        descricao: descricao,
        dataInicio: dataInicio,
        frequencia: frequencia,
        dataFim: dataFim,
        diaVencimento: diaVencimento,
        observacoes: observacoes,
      );

      await loadRecurrences(refresh: true);
      return true;
    } catch (e) {
      _errorMessage = 'Erro ao criar recorrência: ${e.toString()}';
      notifyListeners();
      return false;
    }
  }

  /// Atualiza recorrência existente
  Future<bool> updateRecurrence({
    required int id,
    required double valor,
    required String tipo,
    required int categoriaId,
    required String descricao,
    required DateTime dataInicio,
    required String frequencia,
    DateTime? dataFim,
    int? diaVencimento,
    String? observacoes,
    bool? ativa,
  }) async {
    try {
      await _repository.updateRecurrence(
        id: id,
        valor: valor,
        tipo: tipo,
        categoriaId: categoriaId,
        descricao: descricao,
        dataInicio: dataInicio,
        frequencia: frequencia,
        dataFim: dataFim,
        diaVencimento: diaVencimento,
        observacoes: observacoes,
        ativa: ativa,
      );

      await loadRecurrences(refresh: true);
      return true;
    } catch (e) {
      _errorMessage = 'Erro ao atualizar recorrência: ${e.toString()}';
      notifyListeners();
      return false;
    }
  }

  /// Deleta recorrência
  Future<bool> deleteRecurrence(int id) async {
    try {
      await _repository.deleteRecurrence(id);

      await loadRecurrences(refresh: true);
      return true;
    } catch (e) {
      _errorMessage = 'Erro ao deletar recorrência: ${e.toString()}';
      notifyListeners();
      return false;
    }
  }

  /// Ativa/desativa recorrência (removido - requer todos os parâmetros)
  // Future<bool> toggleRecurrence(int id, bool activate) async {
  //   try {
  //     await _repository.updateRecurrence(
  //       id: id,
  //       ativa: activate,
  //     );
  //
  //     await loadRecurrences(refresh: true);
  //     return true;
  //   } catch (e) {
  //     _errorMessage = 'Erro ao ${activate ? 'ativar' : 'desativar'} recorrência: ${e.toString()}';
  //     notifyListeners();
  //     return false;
  //   }
  // }

  /// Filtra recorrências por frequência
  List<RecurrenceEntity> filterByFrequency(RecurrenceFrequency frequency) {
    return _recurrences.where((r) => r.frequency == frequency).toList();
  }

  /// Filtra recorrências por tipo
  List<RecurrenceEntity> filterByType(TransactionType type) {
    return _recurrences.where((r) => r.type == type).toList();
  }

  /// Busca recorrências por nome
  List<RecurrenceEntity> search(String query) {
    if (query.isEmpty) return _recurrences;
    final lowerQuery = query.toLowerCase();
    return _recurrences
        .where((r) => r.name.toLowerCase().contains(lowerQuery) ||
            r.categoryName.toLowerCase().contains(lowerQuery))
        .toList();
  }

  /// Obtém recorrência por ID
  RecurrenceEntity? getRecurrenceById(String id) {
    try {
      return _recurrences.firstWhere((r) => r.id == id);
    } catch (e) {
      return null;
    }
  }

  /// Agrupa recorrências por frequência
  Map<RecurrenceFrequency, List<RecurrenceEntity>> groupByFrequency() {
    final map = <RecurrenceFrequency, List<RecurrenceEntity>>{};

    for (var recurrence in _recurrences) {
      if (!map.containsKey(recurrence.frequency)) {
        map[recurrence.frequency] = [];
      }
      map[recurrence.frequency]!.add(recurrence);
    }

    return map;
  }

  /// Calcula total mensal baseado na frequência
  double _calculateMonthlyTotal(List<RecurrenceEntity> recurrences) {
    return recurrences.fold(0.0, (sum, r) {
      if (!r.isActive) return sum;

      switch (r.frequency) {
        case RecurrenceFrequency.daily:
          return sum + (r.amount * 30); // aproximação
        case RecurrenceFrequency.weekly:
          return sum + (r.amount * 4); // 4 semanas
        case RecurrenceFrequency.monthly:
          return sum + r.amount;
        case RecurrenceFrequency.yearly:
          return sum + (r.amount / 12);
      }
    });
  }

  /// Verifica se há alertas (recorrências próximas ou vencidas)
  bool get hasAlerts {
    return upcomingRecurrences.isNotEmpty || overdueRecurrences.isNotEmpty;
  }

  /// Número de alertas
  int get alertCount {
    return upcomingRecurrences.length + overdueRecurrences.length;
  }

  /// Limpa erro
  void clearError() {
    _errorMessage = null;
    notifyListeners();
  }

  /// Converte dados do cache para entidades
  List<RecurrenceEntity> _convertCachedToEntities(List<Map<String, dynamic>> cachedData) {
    return cachedData.map((data) {
      return RecurrenceEntity(
        id: data['id'].toString(),
        name: data['name'] ?? '',
        amount: (data['amount'] as num?)?.toDouble() ?? 0.0,
        type: _parseTransactionType(data['type']),
        categoryName: data['categoryName'] ?? '',
        categoriaId: data['categoriaId'],
        frequency: _parseRecurrenceFrequency(data['frequency']),
        startDate: DateTime.parse(data['startDate']),
        isActive: data['isActive'] ?? true,
        endDate: data['endDate'] != null ? DateTime.parse(data['endDate']) : null,
        diaVencimento: data['diaVencimento'],
        observacoes: data['observacoes'],
      );
    }).toList();
  }

  /// Parse de tipo de transação
  TransactionType _parseTransactionType(String? typeString) {
    if (typeString == null) return TransactionType.expense;
    if (typeString.contains('income')) return TransactionType.income;
    if (typeString.contains('expense')) return TransactionType.expense;
    return TransactionType.expense;
  }

  /// Parse de frequência de recorrência
  RecurrenceFrequency _parseRecurrenceFrequency(String? frequencyString) {
    if (frequencyString == null) return RecurrenceFrequency.monthly;
    if (frequencyString.contains('daily')) return RecurrenceFrequency.daily;
    if (frequencyString.contains('weekly')) return RecurrenceFrequency.weekly;
    if (frequencyString.contains('monthly')) return RecurrenceFrequency.monthly;
    if (frequencyString.contains('yearly')) return RecurrenceFrequency.yearly;
    return RecurrenceFrequency.monthly;
  }
}
