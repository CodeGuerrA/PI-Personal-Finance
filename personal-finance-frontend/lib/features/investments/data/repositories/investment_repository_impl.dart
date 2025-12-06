import '../../domain/entities/investment_entity.dart';
import '../../domain/repositories/investment_repository.dart';
import '../datasources/investment_remote_datasource.dart';

class InvestmentRepositoryImpl implements InvestmentRepository {
  final InvestmentRemoteDataSource _remoteDataSource;

  InvestmentRepositoryImpl({required InvestmentRemoteDataSource remoteDataSource})
      : _remoteDataSource = remoteDataSource;

  @override
  Future<InvestmentEntity> createInvestment({
    required String tipoInvestimento,
    required String nomeAtivo,
    required String simbolo,
    required double quantidade,
    required double valorCompra,
    required DateTime dataCompra,
    required String corretora,
    String? observacoes,
  }) async {
    return await _remoteDataSource.createInvestment(
      tipoInvestimento: tipoInvestimento,
      nomeAtivo: nomeAtivo,
      simbolo: simbolo,
      quantidade: quantidade,
      valorCompra: valorCompra,
      dataCompra: dataCompra,
      corretora: corretora,
      observacoes: observacoes,
    );
  }

  @override
  Future<InvestmentEntity> getInvestmentById(int id) async {
    return await _remoteDataSource.getInvestmentById(id);
  }

  @override
  Future<List<InvestmentEntity>> getAllInvestments() async {
    final models = await _remoteDataSource.getAllInvestments();
    return models.cast<InvestmentEntity>();
  }

  @override
  Future<List<InvestmentEntity>> getInvestmentsByAtivo(bool status) async {
    final models = await _remoteDataSource.getInvestmentsByAtivo(status);
    return models.cast<InvestmentEntity>();
  }

  @override
  Future<List<InvestmentEntity>> getInvestmentsByTipo(String tipo) async {
    final models = await _remoteDataSource.getInvestmentsByTipo(tipo);
    return models.cast<InvestmentEntity>();
  }

  @override
  Future<List<InvestmentEntity>> getInvestmentsByCorretora(String nome) async {
    final models = await _remoteDataSource.getInvestmentsByCorretora(nome);
    return models.cast<InvestmentEntity>();
  }

  @override
  Future<InvestmentEntity> updateInvestment({
    required int id,
    required String nomeAtivo,
    required String simbolo,
    required double quantidade,
    required double valorCompra,
    required DateTime dataCompra,
    required String corretora,
    String? observacoes,
  }) async {
    return await _remoteDataSource.updateInvestment(
      id: id,
      nomeAtivo: nomeAtivo,
      simbolo: simbolo,
      quantidade: quantidade,
      valorCompra: valorCompra,
      dataCompra: dataCompra,
      corretora: corretora,
      observacoes: observacoes,
    );
  }

  @override
  Future<void> deleteInvestment(int id) async {
    return await _remoteDataSource.deleteInvestment(id);
  }
}
