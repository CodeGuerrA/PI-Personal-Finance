import 'package:flutter/material.dart';
import 'package:sgfi/features/investment_movements/domain/entities/investment_movement_entity.dart';
import 'package:sgfi/features/investment_movements/domain/repositories/investment_movement_repository.dart';

/// Provider para gerenciar estado de movimentações de investimentos
class InvestmentMovementProvider extends ChangeNotifier {
  final InvestmentMovementRepository _repository;

  InvestmentMovementProvider(this._repository);

  // Estado
  List<InvestmentMovementEntity> _movements = [];
  bool _isLoading = false;
  String? _errorMessage;

  // Getters
  List<InvestmentMovementEntity> get movements => _movements;
  bool get isLoading => _isLoading;
  String? get errorMessage => _errorMessage;

  /// Movimentações de compra
  List<InvestmentMovementEntity> get buyMovements =>
      _movements.where((m) => m.type == MovementType.compra).toList();

  /// Movimentações de venda
  List<InvestmentMovementEntity> get sellMovements =>
      _movements.where((m) => m.type == MovementType.venda).toList();

  /// Movimentações de dividendos
  List<InvestmentMovementEntity> get dividendMovements =>
      _movements.where((m) => m.type == MovementType.dividendo).toList();

  /// Total de dividendos recebidos
  double get totalDividends {
    return dividendMovements.fold(0.0, (sum, m) => sum + m.totalValue);
  }

  /// Carrega movimentações
  Future<void> loadMovements({bool refresh = false, int? investmentId}) async {
    if (_isLoading) return;

    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      // TODO: Implementar cache para movements
      List<InvestmentMovementEntity> movements;

      if (investmentId != null) {
        movements = await _repository.getMovementsByInvestment(investmentId);
      } else {
        movements = await _repository.getAllMovements();
      }

      _movements = movements;
      _isLoading = false;
      notifyListeners();
    } catch (e) {
      _errorMessage = 'Erro ao carregar movimentações: ${e.toString()}';
      _isLoading = false;
      notifyListeners();
    }
  }

  /// Cria nova movimentação
  Future<bool> createMovement({
    required int investmentId,
    required String tipoMovimentacao,
    required double quantidade,
    required double valorUnitario,
    required double valorTotal,
    required double taxas,
    required DateTime dataMovimentacao,
    String? observacoes,
  }) async {
    try {
      await _repository.createMovement(
        investmentId: investmentId,
        tipoMovimentacao: tipoMovimentacao,
        quantidade: quantidade,
        valorUnitario: valorUnitario,
        valorTotal: valorTotal,
        taxas: taxas,
        dataMovimentacao: dataMovimentacao,
        observacoes: observacoes,
      );

      await loadMovements(refresh: true);
      return true;
    } catch (e) {
      _errorMessage = 'Erro ao criar movimentação: ${e.toString()}';
      notifyListeners();
      return false;
    }
  }

  /// Atualiza movimentação existente
  Future<bool> updateMovement({
    required int id,
    required double quantidade,
    required double valorUnitario,
    required double valorTotal,
    required double taxas,
    required DateTime dataMovimentacao,
    String? observacoes,
  }) async {
    try {
      await _repository.updateMovement(
        id: id,
        quantidade: quantidade,
        valorUnitario: valorUnitario,
        valorTotal: valorTotal,
        taxas: taxas,
        dataMovimentacao: dataMovimentacao,
        observacoes: observacoes,
      );

      await loadMovements(refresh: true);
      return true;
    } catch (e) {
      _errorMessage = 'Erro ao atualizar movimentação: ${e.toString()}';
      notifyListeners();
      return false;
    }
  }

  /// Deleta movimentação
  Future<bool> deleteMovement(int id) async {
    try {
      await _repository.deleteMovement(id);

      await loadMovements(refresh: true);
      return true;
    } catch (e) {
      _errorMessage = 'Erro ao deletar movimentação: ${e.toString()}';
      notifyListeners();
      return false;
    }
  }

  /// Filtra movimentações por tipo
  List<InvestmentMovementEntity> filterByType(MovementType type) {
    return _movements.where((m) => m.type == type).toList();
  }

  /// Filtra movimentações por investimento
  List<InvestmentMovementEntity> filterByInvestment(String investmentId) {
    return _movements.where((m) => m.investmentId == investmentId).toList();
  }

  /// Filtra movimentações por período
  List<InvestmentMovementEntity> filterByDateRange(DateTimeRange range) {
    return _movements.where((m) {
      return (m.date.isAtSameMomentAs(range.start) ||
              m.date.isAfter(range.start)) &&
          (m.date.isAtSameMomentAs(range.end) ||
              m.date.isBefore(range.end.add(const Duration(days: 1))));
    }).toList();
  }

  /// Obtém movimentação por ID
  InvestmentMovementEntity? getMovementById(String id) {
    try {
      return _movements.firstWhere((m) => m.id == id);
    } catch (e) {
      return null;
    }
  }

  /// Agrupa movimentações por investimento
  Map<String, List<InvestmentMovementEntity>> groupByInvestment() {
    final map = <String, List<InvestmentMovementEntity>>{};

    for (var movement in _movements) {
      if (!map.containsKey(movement.investmentId)) {
        map[movement.investmentId] = [];
      }
      map[movement.investmentId]!.add(movement);
    }

    return map;
  }

  /// Calcula total investido (compras - vendas)
  double calculateNetInvested() {
    final buys = buyMovements.fold(0.0, (sum, m) => sum + m.totalValue);
    final sells = sellMovements.fold(0.0, (sum, m) => sum + m.totalValue);
    return buys - sells;
  }

  /// Histórico de movimentações ordenado por data
  List<InvestmentMovementEntity> get sortedByDate {
    final sorted = [..._movements];
    sorted.sort((a, b) => b.date.compareTo(a.date));
    return sorted;
  }

  /// Movimentações do mês atual
  List<InvestmentMovementEntity> get currentMonthMovements {
    final now = DateTime.now();
    return _movements.where((m) {
      return m.date.year == now.year && m.date.month == now.month;
    }).toList();
  }

  /// Limpa erro
  void clearError() {
    _errorMessage = null;
    notifyListeners();
  }
}
