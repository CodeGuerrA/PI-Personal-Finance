import 'dart:convert';
import '../../../../core/network/api_client.dart';
import '../models/investment_model.dart';
import 'investment_remote_datasource.dart';

class InvestmentRemoteDataSourceImpl implements InvestmentRemoteDataSource {
  final ApiClient _apiClient;

  InvestmentRemoteDataSourceImpl({ApiClient? apiClient})
      : _apiClient = apiClient ?? ApiClient();

  @override
  Future<InvestmentModel> createInvestment({
    required String tipoInvestimento,
    required String nomeAtivo,
    required String simbolo,
    required double quantidade,
    required double valorCompra,
    required DateTime dataCompra,
    required String corretora,
    String? observacoes,
  }) async {
    final valorTotalInvestido = quantidade * valorCompra;

    final response = await _apiClient.post(
      '/investments',
      body: {
        'tipoInvestimento': tipoInvestimento,
        'nomeAtivo': nomeAtivo,
        'simbolo': simbolo,
        'quantidade': quantidade,
        'valorCompra': valorCompra,
        'valorTotalInvestido': valorTotalInvestido,
        'dataCompra': dataCompra.toIso8601String().split('T')[0],
        'corretora': corretora,
        if (observacoes != null) 'observacoes': observacoes,
      },
    );

    final investment = _apiClient.parseResponse<InvestmentModel>(
      response,
      (json) => InvestmentModel.fromJson(json),
    );

    if (investment != null) {
      return investment;
    }

    throw Exception('Erro ao criar investimento');
  }

  @override
  Future<InvestmentModel> getInvestmentById(int id) async {
    final response = await _apiClient.get('/investments/$id');

    final investment = _apiClient.parseResponse<InvestmentModel>(
      response,
      (json) => InvestmentModel.fromJson(json),
    );

    if (investment != null) {
      return investment;
    }

    throw Exception('Investimento não encontrado');
  }

  @override
  Future<List<InvestmentModel>> getAllInvestments() async {
    final response = await _apiClient.get('/investments');

    return _apiClient.parseListResponse<InvestmentModel>(
      response,
      (json) => InvestmentModel.fromJson(json),
    );
  }

  @override
  Future<List<InvestmentModel>> getInvestmentsByAtivo(bool status) async {
    final response = await _apiClient.get(
      '/investments/ativo',
      queryParameters: {'status': status.toString()},
    );

    return _apiClient.parseListResponse<InvestmentModel>(
      response,
      (json) => InvestmentModel.fromJson(json),
    );
  }

  @override
  Future<List<InvestmentModel>> getInvestmentsByTipo(String tipo) async {
    final response = await _apiClient.get('/investments/tipo/$tipo');

    return _apiClient.parseListResponse<InvestmentModel>(
      response,
      (json) => InvestmentModel.fromJson(json),
    );
  }

  @override
  Future<List<InvestmentModel>> getInvestmentsByCorretora(String nome) async {
    final response = await _apiClient.get(
      '/investments/corretora',
      queryParameters: {'nome': nome},
    );

    return _apiClient.parseListResponse<InvestmentModel>(
      response,
      (json) => InvestmentModel.fromJson(json),
    );
  }

  @override
  Future<InvestmentModel> updateInvestment({
    required int id,
    required String nomeAtivo,
    required String simbolo,
    required double quantidade,
    required double valorCompra,
    required DateTime dataCompra,
    required String corretora,
    String? observacoes,
  }) async {
    final valorTotalInvestido = quantidade * valorCompra;

    final response = await _apiClient.put(
      '/investments/$id',
      body: {
        'quantidade': quantidade,
        'valorCompra': valorCompra,
        'valorTotalInvestido': valorTotalInvestido,
        if (observacoes != null) 'observacoes': observacoes,
      },
    );

    final investment = _apiClient.parseResponse<InvestmentModel>(
      response,
      (json) => InvestmentModel.fromJson(json),
    );

    if (investment != null) {
      return investment;
    }

    throw Exception('Erro ao atualizar investimento');
  }

  @override
  Future<void> deleteInvestment(int id) async {
    await _apiClient.delete('/investments/$id');
  }

  @override
  Future<List<InvestmentModel>> getInvestmentsByPeriod({
    required DateTime dataInicio,
    required DateTime dataFim,
  }) async {
    final response = await _apiClient.get(
      '/investments/periodo',
      queryParameters: {
        'dataInicio': dataInicio.toIso8601String().split('T')[0],
        'dataFim': dataFim.toIso8601String().split('T')[0],
      },
    );

    return _apiClient.parseListResponse<InvestmentModel>(
      response,
      (json) => InvestmentModel.fromJson(json),
    );
  }

  @override
  Future<List<InvestmentModel>> getInvestmentsBySymbol(String codigo) async {
    final response = await _apiClient.get(
      '/investments/simbolo',
      queryParameters: {'codigo': codigo},
    );

    return _apiClient.parseListResponse<InvestmentModel>(
      response,
      (json) => InvestmentModel.fromJson(json),
    );
  }

  @override
  Future<List<InvestmentModel>> getInvestmentsOrderByValue() async {
    final response = await _apiClient.get('/investments/ordenar/valor');

    return _apiClient.parseListResponse<InvestmentModel>(
      response,
      (json) => InvestmentModel.fromJson(json),
    );
  }

  @override
  Future<List<InvestmentModel>> getInvestmentsOrderByDate() async {
    final response = await _apiClient.get('/investments/ordenar/data');

    return _apiClient.parseListResponse<InvestmentModel>(
      response,
      (json) => InvestmentModel.fromJson(json),
    );
  }

  @override
  Future<Map<String, dynamic>> getStatistics() async {
    final response = await _apiClient.get('/investments/estatisticas');

    if (response.statusCode >= 200 && response.statusCode < 300) {
      final json = jsonDecode(response.body);
      return json as Map<String, dynamic>;
    }

    throw Exception('Erro ao buscar estatísticas');
  }
}
