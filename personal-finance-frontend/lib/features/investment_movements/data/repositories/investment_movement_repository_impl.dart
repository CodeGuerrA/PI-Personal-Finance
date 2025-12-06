import '../../domain/entities/investment_movement_entity.dart';
import '../../domain/repositories/investment_movement_repository.dart';
import '../datasources/investment_movement_remote_datasource.dart';

class InvestmentMovementRepositoryImpl implements InvestmentMovementRepository {
  final InvestmentMovementRemoteDataSource _remoteDataSource;

  InvestmentMovementRepositoryImpl({
    required InvestmentMovementRemoteDataSource remoteDataSource,
  }) : _remoteDataSource = remoteDataSource;

  @override
  Future<InvestmentMovementEntity> createMovement({
    required int investmentId,
    required String tipoMovimentacao,
    required double quantidade,
    required double valorUnitario,
    required double valorTotal,
    required double taxas,
    required DateTime dataMovimentacao,
    String? observacoes,
  }) async {
    return await _remoteDataSource.createMovement(
      investmentId: investmentId,
      tipoMovimentacao: tipoMovimentacao,
      quantidade: quantidade,
      valorUnitario: valorUnitario,
      valorTotal: valorTotal,
      taxas: taxas,
      dataMovimentacao: dataMovimentacao,
      observacoes: observacoes,
    );
  }

  @override
  Future<InvestmentMovementEntity> getMovementById(int id) async {
    return await _remoteDataSource.getMovementById(id);
  }

  @override
  Future<List<InvestmentMovementEntity>> getAllMovements() async {
    final models = await _remoteDataSource.getAllMovements();
    return models.cast<InvestmentMovementEntity>();
  }

  @override
  Future<List<InvestmentMovementEntity>> getMovementsByInvestment(
      int investmentId) async {
    final models = await _remoteDataSource.getMovementsByInvestment(investmentId);
    return models.cast<InvestmentMovementEntity>();
  }

  @override
  Future<InvestmentMovementEntity> updateMovement({
    required int id,
    required double quantidade,
    required double valorUnitario,
    required double valorTotal,
    required double taxas,
    required DateTime dataMovimentacao,
    String? observacoes,
  }) async {
    return await _remoteDataSource.updateMovement(
      id: id,
      quantidade: quantidade,
      valorUnitario: valorUnitario,
      valorTotal: valorTotal,
      taxas: taxas,
      dataMovimentacao: dataMovimentacao,
      observacoes: observacoes,
    );
  }

  @override
  Future<void> deleteMovement(int id) async {
    return await _remoteDataSource.deleteMovement(id);
  }
}
