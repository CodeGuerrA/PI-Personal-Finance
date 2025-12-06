import '../entities/investment_movement_entity.dart';

abstract class InvestmentMovementRepository {
  Future<InvestmentMovementEntity> createMovement({
    required int investmentId,
    required String tipoMovimentacao,
    required double quantidade,
    required double valorUnitario,
    required double valorTotal,
    required double taxas,
    required DateTime dataMovimentacao,
    String? observacoes,
  });

  Future<InvestmentMovementEntity> getMovementById(int id);

  Future<List<InvestmentMovementEntity>> getAllMovements();

  Future<List<InvestmentMovementEntity>> getMovementsByInvestment(
      int investmentId);

  Future<InvestmentMovementEntity> updateMovement({
    required int id,
    required double quantidade,
    required double valorUnitario,
    required double valorTotal,
    required double taxas,
    required DateTime dataMovimentacao,
    String? observacoes,
  });

  Future<void> deleteMovement(int id);
}
