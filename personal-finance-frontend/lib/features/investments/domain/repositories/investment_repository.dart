import '../entities/investment_entity.dart';

abstract class InvestmentRepository {
  Future<InvestmentEntity> createInvestment({
    required String tipoInvestimento,
    required String nomeAtivo,
    required String simbolo,
    required double quantidade,
    required double valorCompra,
    required DateTime dataCompra,
    required String corretora,
    String? observacoes,
  });

  Future<InvestmentEntity> getInvestmentById(int id);

  Future<List<InvestmentEntity>> getAllInvestments();

  Future<List<InvestmentEntity>> getInvestmentsByAtivo(bool status);

  Future<List<InvestmentEntity>> getInvestmentsByTipo(String tipo);

  Future<List<InvestmentEntity>> getInvestmentsByCorretora(String nome);

  Future<InvestmentEntity> updateInvestment({
    required int id,
    required String nomeAtivo,
    required String simbolo,
    required double quantidade,
    required double valorCompra,
    required DateTime dataCompra,
    required String corretora,
    String? observacoes,
  });

  Future<void> deleteInvestment(int id);
}
