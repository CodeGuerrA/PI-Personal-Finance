import 'package:flutter/material.dart';
import 'package:sgfi/features/goals/domain/entities/goal_entity.dart';
import 'package:sgfi/features/goals/domain/repositories/goal_repository.dart';
import 'package:sgfi/core/cache/cache_service.dart';

/// Provider para gerenciar estado de objetivos/metas
class GoalProvider extends ChangeNotifier {
  final GoalRepository _repository;

  GoalProvider(this._repository);

  // Estado
  List<GoalEntity> _goals = [];
  bool _isLoading = false;
  String? _errorMessage;

  // Getters
  List<GoalEntity> get goals => _goals;
  bool get isLoading => _isLoading;
  String? get errorMessage => _errorMessage;

  /// Metas ativas
  List<GoalEntity> get activeGoals =>
      _goals.where((g) => g.isActive).toList();

  /// Metas concluídas (100%)
  List<GoalEntity> get completedGoals =>
      _goals.where((g) => g.progress * 100 >= 100).toList();

  /// Metas próximas de cumprir (>= 80%)
  List<GoalEntity> get nearCompletionGoals =>
      _goals.where((g) => g.progress * 100 >= 80 && g.progress * 100 < 100).toList();

  /// Metas com limite ultrapassado (despesas)
  List<GoalEntity> get exceededGoals =>
      _goals.where((g) => g.type == ObjectiveType.limiteCategoria && g.progress * 100 > 100).toList();

  /// Progresso geral de todas as metas
  double get overallProgress {
    if (_goals.isEmpty) return 0;

    final totalTarget = _goals.fold<double>(0, (sum, g) => sum + g.targetAmount);
    final totalCurrent = _goals.fold<double>(0, (sum, g) => sum + g.currentAmount);

    if (totalTarget <= 0) return 0;

    return (totalCurrent / totalTarget).clamp(0.0, 1.0) * 100;
  }

  /// Carrega metas
  Future<void> loadGoals({bool refresh = false}) async {
    if (_isLoading) return;

    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      // Tentar cache primeiro se não for refresh forçado
      if (!refresh) {
        final cachedData = CacheService.getCachedGoals();
        if (cachedData != null && CacheService.isGoalsCacheValid()) {
          _goals = _convertCachedToEntities(cachedData);
          _isLoading = false;
          notifyListeners();
          return;
        }
      }

      // Buscar do backend
      final goals = await _repository.getAllGoals();

      // Salvar no cache
      await CacheService.cacheGoals(goals);

      _goals = goals;
      _isLoading = false;
      notifyListeners();
    } catch (e) {
      _errorMessage = 'Erro ao carregar metas: ${e.toString()}';
      _isLoading = false;

      // Fallback: tentar usar cache expirado em caso de erro de rede
      if (!refresh) {
        final cachedData = CacheService.getCachedGoals();
        if (cachedData != null) {
          _goals = _convertCachedToEntities(cachedData);
        }
      }

      notifyListeners();
    }
  }

  /// Cria novo objetivo
  Future<bool> createGoal({
    required int categoriaId,
    required String descricao,
    required double valorObjetivo,
    required String mesAno,
    required String tipo,
  }) async {
    try {
      // Converter string para ObjectiveType
      final objectiveType = _parseObjectiveType(tipo);

      if (objectiveType == null) {
        _errorMessage = 'Tipo de objetivo inválido';
        notifyListeners();
        return false;
      }

      await _repository.createGoal(
        categoriaId: categoriaId,
        descricao: descricao,
        valorObjetivo: valorObjetivo,
        mesAno: mesAno,
        tipo: objectiveType,
      );

      await loadGoals(refresh: true);
      return true;
    } catch (e) {
      _errorMessage = 'Erro ao criar meta: ${e.toString()}';
      notifyListeners();
      return false;
    }
  }

  /// Atualiza objetivo existente
  Future<bool> updateGoal({
    required int id,
    String? descricao,
    double? valorObjetivo,
    double? valorAtual,
    bool? ativa,
  }) async {
    try {
      // Se estiver atualizando apenas o valor atual
      if (descricao == null && valorObjetivo == null && valorAtual != null) {
        await _repository.updateGoalValue(
          id: id,
          novoValor: valorAtual,
        );
      }
      // Se estiver atualizando descrição/valor objetivo/valor atual completo
      else if (descricao != null && valorObjetivo != null && valorAtual != null) {
        await _repository.updateGoal(
          id: id,
          descricao: descricao,
          valorObjetivo: valorObjetivo,
          valorAtual: valorAtual,
        );
      }

      await loadGoals(refresh: true);
      return true;
    } catch (e) {
      _errorMessage = 'Erro ao atualizar meta: ${e.toString()}';
      notifyListeners();
      return false;
    }
  }

  /// Desativa objetivo
  Future<bool> deactivateGoal(int id) async {
    try {
      await _repository.deactivateGoal(id);

      await loadGoals(refresh: true);
      return true;
    } catch (e) {
      _errorMessage = 'Erro ao desativar meta: ${e.toString()}';
      notifyListeners();
      return false;
    }
  }

  /// Filtra metas por tipo
  List<GoalEntity> filterByType(ObjectiveType type) {
    return _goals.where((g) => g.type == type).toList();
  }

  /// Filtra metas por mês/ano (removido - campo não existe na entidade)
  // List<GoalEntity> filterByMonth(String mesAno) {
  //   return _goals.where((g) => g.dueDate == mesAno).toList();
  // }

  /// Busca metas por nome
  List<GoalEntity> search(String query) {
    if (query.isEmpty) return _goals;
    final lowerQuery = query.toLowerCase();
    return _goals
        .where((g) => g.name.toLowerCase().contains(lowerQuery))
        .toList();
  }

  /// Obtém meta por ID
  GoalEntity? getGoalById(String id) {
    try {
      return _goals.firstWhere((g) => g.id == id);
    } catch (e) {
      return null;
    }
  }

  /// Obtém metas do mês atual (removido - campo monthYear não existe na entidade)
  // List<GoalEntity> getCurrentMonthGoals() {
  //   final now = DateTime.now();
  //   final mesAno = '${now.year}-${now.month.toString().padLeft(2, '0')}';
  //   return filterByMonth(mesAno);
  // }

  /// Verifica se há alertas (limites ultrapassados ou metas próximas)
  bool get hasAlerts {
    return exceededGoals.isNotEmpty || nearCompletionGoals.isNotEmpty;
  }

  /// Número de alertas
  int get alertCount {
    return exceededGoals.length + nearCompletionGoals.length;
  }

  /// Limpa erro
  void clearError() {
    _errorMessage = null;
    notifyListeners();
  }

  /// Invalida todos os dados (força recarregamento na próxima requisição)
  void invalidate() {
    print('GoalProvider - Invalidando dados...');
    _goals = [];
    _errorMessage = null;
    notifyListeners();
  }

  /// Converte dados do cache para entidades
  List<GoalEntity> _convertCachedToEntities(List<Map<String, dynamic>> cachedData) {
    return cachedData.map((data) {
      return GoalEntity(
        id: data['id'].toString(),
        name: data['name'] ?? '',
        targetAmount: (data['targetAmount'] as num?)?.toDouble() ?? 0.0,
        currentAmount: (data['currentAmount'] as num?)?.toDouble() ?? 0.0,
        isActive: data['isActive'] ?? true,
        dueDate: data['dueDate'] != null ? DateTime.parse(data['dueDate']) : null,
        type: _parseObjectiveType(data['type']),
        percentualAtingido: (data['percentualAtingido'] as num?)?.toDouble(),
        saldoRestante: (data['saldoRestante'] as num?)?.toDouble(),
        statusAlerta: data['statusAlerta'],
      );
    }).toList();
  }

  /// Parse de tipo de objetivo
  ObjectiveType? _parseObjectiveType(String? typeString) {
    if (typeString == null) return null;

    // Normalizar para maiúsculas para comparação
    final normalized = typeString.toUpperCase();

    if (normalized.contains('LIMITE') && normalized.contains('CATEGORIA')) {
      return ObjectiveType.limiteCategoria;
    }
    if (normalized.contains('ECONOMIA') && normalized.contains('MES')) {
      return ObjectiveType.metaEconomiaMes;
    }
    if (normalized.contains('INVESTIMENTO')) {
      return ObjectiveType.metaInvestimento;
    }

    return null;
  }
}
