import '../models/investment_movement_model.dart';

abstract class InvestmentMovementRemoteDataSource {
  Future<InvestmentMovementModel> createMovement({
    required int investmentId,
    required String tipoMovimentacao,
    required double quantidade,
    required double valorUnitario,
    required double valorTotal,
    required double taxas,
    required DateTime dataMovimentacao,
    String? observacoes,
  });

  Future<InvestmentMovementModel> getMovementById(int id);

  Future<List<InvestmentMovementModel>> getAllMovements();

  Future<List<InvestmentMovementModel>> getMovementsByInvestment(int investmentId);

  Future<InvestmentMovementModel> updateMovement({
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
