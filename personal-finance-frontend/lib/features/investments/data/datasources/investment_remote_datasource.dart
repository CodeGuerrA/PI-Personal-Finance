import '../models/investment_model.dart';

abstract class InvestmentRemoteDataSource {
  Future<InvestmentModel> createInvestment({
    required String tipoInvestimento,
    required String nomeAtivo,
    required String simbolo,
    required double quantidade,
    required double valorCompra,
    required DateTime dataCompra,
    required String corretora,
    String? observacoes,
  });

  Future<InvestmentModel> getInvestmentById(int id);

  Future<List<InvestmentModel>> getAllInvestments();

  Future<List<InvestmentModel>> getInvestmentsByAtivo(bool status);

  Future<List<InvestmentModel>> getInvestmentsByTipo(String tipo);

  Future<List<InvestmentModel>> getInvestmentsByCorretora(String nome);

  Future<List<InvestmentModel>> getInvestmentsByPeriod({
    required DateTime dataInicio,
    required DateTime dataFim,
  });

  Future<List<InvestmentModel>> getInvestmentsBySymbol(String codigo);

  Future<List<InvestmentModel>> getInvestmentsOrderByValue();

  Future<List<InvestmentModel>> getInvestmentsOrderByDate();

  Future<Map<String, dynamic>> getStatistics();

  Future<InvestmentModel> updateInvestment({
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
